import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

Global / onChangedBuildSource := ReloadOnSourceChanges

val Scala212 = "2.12.15"
val Scala213 = "2.13.8"
val Scala3 = "3.1.0"

val Scala212Cond = s"matrix.scala == '$Scala212'"

ThisBuild / scalaVersion := Scala213
ThisBuild / crossScalaVersions := Seq(Scala212, Scala213, Scala3)

def rubySetupSteps(cond: Option[String]) = Seq(
  WorkflowStep.Use(
    UseRef.Public("ruby", "setup-ruby", "v1"),
    name = Some("Setup Ruby"),
    params = Map("ruby-version" -> "2.6.0"),
    cond = cond
  ),
  WorkflowStep.Run(
    List("gem install saas", "gem install jekyll -v 4.2.0"),
    name = Some("Install microsite dependencies"),
    cond = cond
  )
)

val PrimaryJava = JavaSpec.temurin("8")
val LTSJava = JavaSpec.temurin("17")

ThisBuild / githubWorkflowPublishTargetBranches := Seq()
ThisBuild / githubWorkflowJavaVersions := Seq(PrimaryJava, LTSJava)
ThisBuild / githubWorkflowBuild := Seq(
  WorkflowStep
    .Sbt(
      List("scalafmtCheckAll", "scalafmtSbtCheck"),
      name = Some("Check formatting")
    ),
  WorkflowStep.Sbt(List("mimaReportBinaryIssues"), name = Some("Check binary issues")),
  WorkflowStep.Sbt(List("Test/compile"), name = Some("Compile")),
  WorkflowStep.Sbt(List("test"), name = Some("Run tests")),
  WorkflowStep.Sbt(List("doc"), name = Some("Build the Scaladoc")),
  WorkflowStep.Sbt(
    List("docs/makeMicrosite"),
    name = Some("Build the Microsite"),
    cond = Some(Scala212Cond)
  )
)

ThisBuild / githubWorkflowBuildPreamble ++=
  rubySetupSteps(Some(Scala212Cond))

ThisBuild / githubWorkflowTargetTags ++= Seq("v*")

// currently only publishing tags
ThisBuild / githubWorkflowPublishTargetBranches :=
  Seq(RefPredicate.StartsWith(Ref.Tag("v")))

ThisBuild / githubWorkflowPublishPreamble ++=
  rubySetupSteps(None)

ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(List("release"), name = Some("Release")),
  WorkflowStep.Sbt(
    List(s"++${Scala212}", "docs/publishMicrosite"),
    name = Some(s"Publish microsite")
  )
)

lazy val monoids = project
  .in(file("."))
  .settings(commonSettings, skipOnPublishSettings)
  .aggregate(core.jvm, core.js)
  .enablePlugins(NoPublishPlugin)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .enablePlugins(SonatypeCiReleasePlugin)
  .settings(commonSettings, mimaSettings)
  .settings(
    name := "monoids"
  )
  .jsSettings(
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule))
  )

lazy val docs = project
  .in(file("docs"))
  .settings(
    commonSettings,
    skipOnPublishSettings,
    micrositeSettings,
    publish / skip := true,
    githubWorkflowArtifactUpload := false
  )
  .dependsOn(core.jvm)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(MdocPlugin)
  .enablePlugins(NoPublishPlugin)
  .settings(mdocIn := sourceDirectory.value / "main" / "mdoc")

val catsV = "2.7.0"

val kindProjectorV = "0.13.2"
val betterMonadicForV = "0.3.1"

lazy val contributors = Seq(
  "ChristopherDavenport" -> "Christopher Davenport"
)

def scalaVersionSpecificFolders(srcName: String, srcBaseDir: java.io.File, scalaVersion: String) = {
  def extraDirs(suffix: String) =
    List(CrossType.Pure, CrossType.Full)
      .flatMap(_.sharedSrcDir(srcBaseDir, srcName).toList.map(f => file(f.getPath + suffix)))

  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, _)) => extraDirs("-2.x")
    case Some((0 | 3, _)) => extraDirs("-3.x")
    case _ => Nil
  }
}

// General Settings
lazy val commonSettings = Seq(
  organization := "org.typelevel",
  Compile / unmanagedSourceDirectories ++= scalaVersionSpecificFolders(
    "main",
    baseDirectory.value,
    scalaVersion.value
  ),
  Test / unmanagedSourceDirectories ++= scalaVersionSpecificFolders(
    "test",
    baseDirectory.value,
    scalaVersion.value
  ),
  scalacOptions := scalacOptions.value.filterNot(_ == "-source:3.0-migration"),
  scalacOptions --= (
    if (ScalaArtifacts.isScala3(scalaVersion.value))
      Seq(
        "-deprecation",
        "-encoding",
        "-feature",
        "-Ykind-projector",
        "-unchecked",
        "-language:implicitConversions"
      )
    else Nil
  ),
  scalacOptions ++= (
    if (ScalaArtifacts.isScala3(scalaVersion.value)) Nil
    else Seq("-Yrangepos", "-language:higherKinds")
  ),
  libraryDependencies ++= (
    if (ScalaArtifacts.isScala3(scalaVersion.value)) Nil
    else
      Seq(
        compilerPlugin(
          ("org.typelevel" %% "kind-projector" % kindProjectorV).cross(CrossVersion.full)
        ),
        compilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForV)
      )
  ),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-core"        % catsV,
    "org.typelevel" %%% "cats-testkit"     % catsV   % Test,
    "org.typelevel" %%% "discipline-munit" % "1.0.9" % Test
  ),
  testFrameworks += new TestFramework("munit.Framework")
)

inThisBuild(
  List(
    organization := "org.typelevel",
    organizationName := "Typelevel",
    baseVersion := "0.2.0",
    publishGithubUser := "rossabaker",
    publishFullName := "Ross A. Baker",
    homepage := Some(url("https://github.com/typelevel/monoids")),
    scmInfo := Some(ScmInfo(
      url("https://github.com/typelevel/monoids"),
      "git@github.com:typelevel/monoids.git"
    )),
    licenses := List("MIT" -> url("https://opensource.org/licenses/MIT")),
    developers := {
      (for ((username, name) <- contributors)
        yield Developer(username, name, "", url(s"http://github.com/$username"))).toList
    }
  )
)

// Not Used Currently
lazy val mimaSettings = {
  def mimaVersion(version: String) = {
    VersionNumber(version) match {
      case VersionNumber(Seq(major, minor, patch, _*), _, _) if patch.toInt > 0 =>
        Some(s"$major.$minor.${patch.toInt - 1}")
      case _ =>
        None
    }
  }

  Seq(
    mimaFailOnProblem := mimaVersion(version.value).isDefined,
    ThisBuild / mimaFailOnNoPrevious := false,
    mimaPreviousArtifacts := (mimaVersion(version.value) map {
      organization.value % s"${moduleName.value}_${scalaBinaryVersion.value}" % _
    }).toSet,
    mimaBinaryIssueFilters ++= {
      import com.typesafe.tools.mima.core._
      import com.typesafe.tools.mima.core.ProblemFilters._
      Seq()
    }
  )
}

lazy val micrositeSettings = {
  import microsites._
  Seq(
    micrositeName := "monoids",
    micrositeDescription := "Generic Monoids for Scala",
    micrositeAuthor := "Christopher Davenport",
    micrositeGithubOwner := "typelevel",
    micrositeGithubRepo := "monoids",
    micrositeBaseUrl := "/monoids",
    micrositeDocumentationUrl := "https://typelevel.org/monoids",
    micrositeFooterText := None,
    micrositeHighlightTheme := "atom-one-light",
    micrositePalette := Map(
      "brand-primary" -> "#3e5b95",
      "brand-secondary" -> "#294066",
      "brand-tertiary" -> "#2d5799",
      "gray-dark" -> "#49494B",
      "gray" -> "#7B7B7E",
      "gray-light" -> "#E5E5E6",
      "gray-lighter" -> "#F4F3F4",
      "white-color" -> "#FFFFFF"
    ),
    micrositePushSiteWith := GitHub4s,
    micrositeGithubToken := sys.env.get("GITHUB_TOKEN"),
    micrositeExtraMdFiles := Map(
      file("CHANGELOG.md") -> ExtraMdFileConfig(
        "changelog.md",
        "page",
        Map(
          "title" -> "changelog",
          "section" -> "changelog",
          "position" -> "100"
        )
      ),
      file("CODE_OF_CONDUCT.md") -> ExtraMdFileConfig(
        "code-of-conduct.md",
        "page",
        Map(
          "title" -> "code of conduct",
          "section" -> "code of conduct",
          "position" -> "101"
        )
      ),
      file("LICENSE") -> ExtraMdFileConfig(
        "license.md",
        "page",
        Map("title" -> "license", "section" -> "license", "position" -> "102")
      )
    )
  )
}

lazy val skipOnPublishSettings = Seq(
  publish / skip := true,
  publish := (()),
  publishLocal := (()),
  publishArtifact := false,
  publishTo := None
)
