package io.chrisdavenport.monoids

opaque type All = Boolean

object All extends AllInstances:
  def apply(getAll: Boolean): All = getAll
  def unapply(arg: All): Option[Boolean] = Some(arg.getAll)

extension (a: All) def getAll: Boolean = a
