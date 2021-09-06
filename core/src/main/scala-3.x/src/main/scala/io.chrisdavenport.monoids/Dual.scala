package io.chrisdavenport.monoids

opaque type Dual[A] = A

object Dual extends DualInstances:
  def apply[A](getDual: A): Dual[A] = getDual
  def unapply[A](arg: Dual[A]): Option[A] = Some(arg.getDual)

extension [A](d: Dual[A]) def getDual: A = d
