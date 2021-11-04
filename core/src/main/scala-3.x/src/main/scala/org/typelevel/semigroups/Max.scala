package org.typelevel.semigroups

opaque type Max[A] = A

object Max extends MaxInstances:
  def apply[A](getMax: A): Max[A] = getMax
  def unapply[A](arg: Max[A]): Option[A] = Some(arg.getMax)

extension [A](m: Max[A])
  def getMax: A = m