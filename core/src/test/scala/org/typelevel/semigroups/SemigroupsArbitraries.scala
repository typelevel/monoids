/*
 * Copyright (c) 2021 Typelevel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
