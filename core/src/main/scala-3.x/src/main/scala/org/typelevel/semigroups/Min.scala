package org.typelevel.semigroups

opaque type Min[A] = A

object Min extends MinInstances:
  def apply[A](getMin: A): Min[A] = getMin
  def unapply[A](arg: Min[A]): Option[A] = Some(arg.getMin)

extension [A](m: Min[A])
  def getMin: A = m
