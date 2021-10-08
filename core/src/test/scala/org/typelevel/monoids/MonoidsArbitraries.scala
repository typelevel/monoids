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

package org.typelevel.monoids

import org.scalacheck._

trait MonoidsArbitraries {
  implicit def functionArb[A, B: Arbitrary]: Arbitrary[A => B] =
    Arbitrary(Arbitrary.arbitrary[B].map(b => (_: A) => b))
  implicit def dualArbitrary[A: Arbitrary]: Arbitrary[Dual[A]] =
    Arbitrary(Arbitrary.arbitrary[A].map(Dual(_)))
  implicit def productArbitrary[A: Arbitrary]: Arbitrary[Product[A]] =
    Arbitrary(Arbitrary.arbitrary[A].map(Product(_)))
  implicit def sumArbitrary[A: Arbitrary]: Arbitrary[Sum[A]] =
    Arbitrary(Arbitrary.arbitrary[A].map(Sum(_)))
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
