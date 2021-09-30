package io.chrisdavenport.monoids

import cats._
import cats.syntax.all._

opaque type Xor[A] = Set[A]

object Xor extends XorInstances:
  def apply[A](getXor: Set[A]): Xor[A] = getXor
  def unapply[A](arg: Xor[A]): Option[Set[A]] = Some(arg.getXor)

extension [A](x: Xor[A])
  def getXor: Set[A] = x
