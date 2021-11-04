package org.typelevel.semigroups

import org.scalacheck._

trait SemigroupsArbitraries {
  implicit def functionArb[A, B: Arbitrary]: Arbitrary[A => B] =
    Arbitrary(Arbitrary.arbitrary[B].map(b => (_: A) => b))
  implicit def dualArbitrary[A: Arbitrary]: Arbitrary[Dual[A]] =
    Arbitrary(Arbitrary.arbitrary[A].map(Dual(_)))
  implicit def firstArbitrary[A: Arbitrary]: Arbitrary[First[A]] =
    Arbitrary(Arbitrary.arbitrary[A].map(First(_)))
  implicit def lastArbitary[A: Arbitrary]: Arbitrary[Last[A]] =
    Arbitrary(Arbitrary.arbitrary[A].map(Last(_)))
  implicit def maxArbitrary[A: Arbitrary]: Arbitrary[Max[A]] =
    Arbitrary(Arbitrary.arbitrary[A].map(Max(_)))
  implicit def minArbitary[A: Arbitrary]: Arbitrary[Min[A]] =
    Arbitrary(Arbitrary.arbitrary[A].map(Min(_)))
  implicit def intersectArbitrary[A: Arbitrary]: Arbitrary[Intersect[A]] =
    Arbitrary(Arbitrary.arbitrary[Set[A]].map(Intersect(_)))
}
