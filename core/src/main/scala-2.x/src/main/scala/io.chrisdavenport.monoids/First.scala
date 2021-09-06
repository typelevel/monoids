package io.chrisdavenport.monoids

final case class First[A](getFirst: Option[A]) extends AnyVal

object First extends FirstInstances
