Global / onChangedBuildSource := ReloadOnSourceChanges

val Scala212 = "2.12.15"
val Scala213 = "2.13.8"
val Scala3 = "3.1.2"

ThisBuild / tlBaseVersion := "0.2"
ThisBuild / licenses := List("MIT" -> url("https://opensource.org/licenses/MIT"))
ThisBuild / startYear := Some(2021)

ThisBuild / scalaVersion := Scala213
ThisBuild / crossScalaVersions := Seq(Scala212, Scala3, Scala213)

val PrimaryJava = JavaSpec.temurin("8")
val LTSJava = JavaSpec.temurin("17")
ThisBuild / githubWorkflowJavaVersions := Seq(PrimaryJava, LTSJava)

lazy val root = tlCrossRootProject.aggregate(core)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(commonSettings)
  .settings(
    name := "monoids"
  )
  .jsSettings(
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule))
  )

lazy val docs = project
  .in(file("site"))
  .settings(
    commonSettings
  )
  .dependsOn(core.jvm)
  .enablePlugins(TypelevelSitePlugin)

val catsV = "2.7.0"

val kindProjectorV = "0.13.2"
val betterMonadicForV = "0.3.1"

ThisBuild / developers ++= List(
  tlGitHubDev("rossabaker", "Ross A. Baker"),
  tlGitHubDev("ChristopherDavenport", "Christopher Davenport")
)

// General Settings
lazy val commonSettings = Seq(
  scalacOptions := scalacOptions.value.filterNot(_ == "-source:3.0-migration"),
  scalacOptions --= (
    if (tlIsScala3.value)
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
    if (tlIsScala3.value) Nil
    else Seq("-Yrangepos", "-language:higherKinds")
  ),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-core"        % catsV,
    "org.typelevel" %%% "cats-testkit"     % catsV    % Test,
    "org.scalameta" %%% "munit"            % "0.7.29" % Test,
    "org.typelevel" %%% "discipline-munit" % "1.0.9"  % Test
  )
)
