package io.chrisdavenport.monoids

import org.scalacheck._

trait MonoidsArbitraries {
  implicit def dualArbitrary[A: Arbitrary]: Arbitrary[Dual[A]] =
    Arbitrary(Arbitrary.arbitrary[A].map(Dual(_)))
  implicit def productArbitrary[A: Arbitrary]: Arbitrary[Product[A]] =
    Arbitrary(Arbitrary.arbitrary[A].map(Product(_)))
  implicit def sumArbitrary[A: Arbitrary]: Arbitrary[Sum[A]] =
    Arbitrary(Arbitrary.arbitrary[A].map(Sum(_)))
}
