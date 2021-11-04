package org.typelevel.semigroups

opaque type Intersect[A] = Set[A]

object Intersect extends IntersectInstances:
  def apply[A](getIntersect: Set[A]): Intersect[A] = getIntersect
  def unapply[A](arg: Intersect[A]): Option[Set[A]] = Some(arg.getIntersect)

extension [A](i: Intersect[A])
  def getIntersect: Set[A] = i
