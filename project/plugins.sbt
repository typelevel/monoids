addSbtPlugin("pl.project13.scala" % "sbt-jmh"                       % "0.4.7")
addSbtPlugin("org.scala-js"       % "sbt-scalajs"                   % "1.16.0")
addSbtPlugin("org.scala-native"   % "sbt-scala-native"              % "0.4.17")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.3.2")
val sbtTypelevelVersion = "0.6.7"
addSbtPlugin("org.typelevel" % "sbt-typelevel"      % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-site" % sbtTypelevelVersion)
