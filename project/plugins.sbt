addSbtPlugin("pl.project13.scala" % "sbt-jmh"                       % "0.4.5")
addSbtPlugin("org.scala-js"       % "sbt-scalajs"                   % "1.13.1")
addSbtPlugin("org.scala-native"   % "sbt-scala-native"              % "0.4.13")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.3.1")
val sbtTypelevelVersion = "0.4.22"
addSbtPlugin("org.typelevel" % "sbt-typelevel"      % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-site" % sbtTypelevelVersion)
