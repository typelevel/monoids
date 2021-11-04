package org.typelevel.semigroups

opaque type First[A] = A

object First extends FirstInstances:
  def apply[A](getFirst: A): First[A] = getFirst
  def unapply[A](arg: First[A]): Option[A] = Some(arg.getFirst)

extension [A](f: First[A])
  def getFirst: A = f
