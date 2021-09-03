import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

Global / onChangedBuildSource := ReloadOnSourceChanges

val Scala212 = "2.12.14"
val Scala213 = "2.13.6"

val Scala212Cond = s"matrix.scala == '$Scala212'"

def rubySetupSteps(cond: Option[String]) = Seq(
  WorkflowStep.Use(UseRef.Public("ruby", "setup-ruby", "v1"),
    name = Some("Setup Ruby"),
    params = Map("ruby-version" -> "2.6.0"),
    cond = cond
  ),
  WorkflowStep.Run(List("gem install saas", "gem install jekyll -v 4.2.0"),
    name = Some("Install microsite dependencies"),
    cond = cond
  )
)

ThisBuild / githubWorkflowPublishTargetBranches := Seq()
ThisBuild / githubWorkflowJavaVersions := Seq("adopt@1.8", "adopt@1.11", "adopt@1.16")
ThisBuild / githubWorkflowBuild := Seq(
  WorkflowStep
    .Sbt(List("scalafmtCheckAll", "scalafmtSbtCheck"), name = Some("Check formatting")),
  WorkflowStep.Sbt(List("mimaReportBinaryIssues"), name = Some("Check binary issues")),
  WorkflowStep.Sbt(List("Test/compile"), name = Some("Compile")),
  WorkflowStep.Sbt(List("test"), name = Some("Run tests")),
  WorkflowStep.Sbt(List("doc"), name = Some("Build the Scaladoc")),
  WorkflowStep.Sbt(List("docs/makeMicrosite"), name = Some("Build the Microsite"), cond = Some(Scala212Cond))
)
ThisBuild / githubWorkflowBuildPreamble ++=
  rubySetupSteps(Some(Scala212Cond))

lazy val monoids = project
  .in(file("."))
  .disablePlugins(MimaPlugin)
  .settings(commonSettings, skipOnPublishSettings)
  .aggregate(core.jvm, core.js)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
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
    micrositeSettings
  )
  .dependsOn(core.jvm)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(MdocPlugin)
  .settings(mdocIn := sourceDirectory.value / "main" / "mdoc")

val catsV = "2.6.1"

val kindProjectorV = "0.13.1"
val betterMonadicForV = "0.3.1"

lazy val contributors = Seq(
  "ChristopherDavenport" -> "Christopher Davenport"
)

// General Settings
lazy val commonSettings = Seq(
  organization := "io.chrisdavenport",
  scalaVersion := Scala213,
  crossScalaVersions := Seq(Scala212, scalaVersion.value),
  scalacOptions ++= Seq("-Yrangepos", "-language:higherKinds"),
  addCompilerPlugin(
    "org.typelevel" % "kind-projector" % kindProjectorV cross CrossVersion.full
  ),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForV),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-core"        % catsV,
    "org.typelevel" %%% "cats-testkit"     % catsV   % Test,
    "org.typelevel" %%% "discipline-munit" % "1.0.9" % Test
  ),
  testFrameworks += new TestFramework("munit.Framework")
)

inThisBuild(
  List(
    organization := "io.chrisdavenport",
    homepage := Some(url("https://github.com/ChristopherDavenport/monoids")),
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
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
    micrositeGithubOwner := "ChristopherDavenport",
    micrositeGithubRepo := "monoids",
    micrositeBaseUrl := "/monoids",
    micrositeDocumentationUrl := "https://www.javadoc.io/doc/io.chrisdavenport/monoids_2.12",
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
