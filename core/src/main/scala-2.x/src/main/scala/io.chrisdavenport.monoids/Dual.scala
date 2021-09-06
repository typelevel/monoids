package io.chrisdavenport.monoids

final case class Dual[A](getDual: A) extends AnyVal

object Dual extends DualInstances
