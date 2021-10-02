package org.typelevel.monoids

final case class Xor[A](getXor: Set[A]) extends AnyVal

object Xor extends XorInstances
