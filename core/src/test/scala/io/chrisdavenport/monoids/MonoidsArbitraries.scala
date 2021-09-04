package io.chrisdavenport.monoids

import org.scalacheck._

trait MonoidsArbitraries {
  implicit def functionArb[A, B: Arbitrary]: Arbitrary[A => B] =
    Arbitrary(Arbitrary.arbitrary[B].map(b => (_: A) => b))
  implicit val allArbitrary: Arbitrary[All] =
    Arbitrary(Arbitrary.arbitrary[Boolean].map(All(_)))
  implicit val anyArbitrary: Arbitrary[Any] =
    Arbitrary(Arbitrary.arbitrary[Boolean].map(Any(_)))
  implicit def firstArbitrary[A: Arbitrary]: Arbitrary[First[A]] =
    Arbitrary(Arbitrary.arbitrary[Option[A]].map(First(_)))
  implicit def lastArbitary[A: Arbitrary]: Arbitrary[Last[A]] =
    Arbitrary(Arbitrary.arbitrary[Option[A]].map(Last(_)))
  implicit def xorArbitrary[A: Arbitrary]: Arbitrary[Xor[A]] =
    Arbitrary(Arbitrary.arbitrary[Set[A]].map(Xor(_)))
}
