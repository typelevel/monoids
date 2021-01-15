import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

Global / onChangedBuildSource := ReloadOnSourceChanges

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

val catsV = "2.3.1"

val kindProjectorV = "0.11.2"

val Scala212 = "2.12.12"
val Scala213 = "2.13.4"
val Scala3 = Seq("3.0.0-M2", "3.0.0-M3")

ThisBuild / crossScalaVersions := Seq(Scala212, Scala213) ++ Scala3
ThisBuild / scalaVersion := Scala213
ThisBuild / organization := "org.typelevel"

ThisBuild / githubWorkflowPublishTargetBranches := Seq()
ThisBuild / githubWorkflowJavaVersions := Seq("adopt@1.8", "adopt@1.11", "adopt@1.15")
ThisBuild / githubWorkflowBuild := Seq(
  WorkflowStep
    .Sbt(List("scalafmtCheckAll", "scalafmtSbtCheck"), name = Some("Check formatting")),
  WorkflowStep.Sbt(List("test:compile"), name = Some("Compile")),
  WorkflowStep.Sbt(List("test"), name = Some("Run tests")),
  WorkflowStep.Sbt(List("doc"), name = Some("Build docs"))
)

lazy val contributors = Seq(
  "ChristopherDavenport" -> "Christopher Davenport"
)

// General Settings
lazy val commonSettings = Seq(
  libraryDependencies ++= (
    if (isDotty.value) Nil
    else
      Seq(
        compilerPlugin(
          ("org.typelevel" %% "kind-projector" % kindProjectorV).cross(CrossVersion.full)
        )
      )
  ),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-core"        % catsV,
    "org.typelevel" %%% "cats-testkit"     % catsV   % Test,
    "org.typelevel" %%% "discipline-munit" % "1.0.4" % Test
  ),
  testFrameworks += new TestFramework("munit.Framework"),
  Compile / doc / sources := { if (isDotty.value) Seq() else (Compile / doc / sources).value }
)

inThisBuild(
  List(
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
        Some(s"${major}.${minor}.${patch.toInt - 1}")
      case _ =>
        None
    }
  }

  Seq(
    mimaFailOnProblem := mimaVersion(version.value).isDefined,
    mimaFailOnNoPrevious in ThisBuild := false,
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
    fork in tut := true,
    scalacOptions in Tut --= Seq(
      "-Xfatal-warnings",
      "-Ywarn-unused-import",
      "-Ywarn-numeric-widen",
      "-Ywarn-dead-code",
      "-Ywarn-unused:imports",
      "-Xlint:-missing-interpolator,_"
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
  skip in publish := true,
  publish := (()),
  publishLocal := (()),
  publishArtifact := false,
  publishTo := None
)
