package org.typelevel.monoids

opaque type Sum[A] = A

object Sum extends SumInstances:
  def apply[A](getSum: A): Sum[A] = getSum
  def unapply[A](arg: Sum[A]): Option[A] = Some(arg.getSum)

extension [A](s: Sum[A])
  def getSum: A = s
