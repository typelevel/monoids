package org.typelevel.monoids

final case class All(getAll: Boolean) extends AnyVal

object All extends AllInstances