package org.typelevel.monoids

opaque type Any = Boolean

object Any extends AnyInstances:
  def apply(getAny: Boolean): Any = getAny
  def unapply(arg: Any): Option[Boolean] = Some(arg.getAny)

extension (a: Any)
  def getAny: Boolean = a
