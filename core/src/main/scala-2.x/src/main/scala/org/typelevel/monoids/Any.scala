package org.typelevel.monoids

final case class Any(getAny: Boolean) extends AnyVal

object Any extends AnyInstances
