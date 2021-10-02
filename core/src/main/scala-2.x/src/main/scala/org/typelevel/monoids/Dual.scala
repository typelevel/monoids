package org.typelevel.monoids

final case class Dual[A](getDual: A) extends AnyVal

object Dual extends DualInstances
