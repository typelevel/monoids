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

val catsV = "2.6.0"

val kindProjectorV = "0.11.3"
val betterMonadicForV = "0.3.1"

lazy val contributors = Seq(
  "ChristopherDavenport" -> "Christopher Davenport"
)

// General Settings
lazy val commonSettings = Seq(
  organization := "io.chrisdavenport",
  scalaVersion := "2.13.5",
  crossScalaVersions := Seq("2.12.13", scalaVersion.value),
  scalacOptions ++= Seq("-Yrangepos", "-language:higherKinds"),
  addCompilerPlugin(
    "org.typelevel" % "kind-projector" % kindProjectorV cross CrossVersion.full
  ),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForV),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-core"        % catsV,
    "org.typelevel" %%% "cats-testkit"     % catsV    % Test,
    "org.typelevel" %%% "discipline-munit" % "1.0.8"  % Test
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
