package org.typelevel.monoids

final case class Sum[A](getSum: A) extends AnyVal

object Sum extends SumInstances
