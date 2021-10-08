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

import cats._
import cats.syntax.all._
import cats.kernel.laws.discipline._
import cats.laws.discipline._

class SumTests extends munit.DisciplineSuite with MonoidsArbitraries {
  checkAll("Sum", EqTests[Sum[Int]].eqv)
  checkAll("Sum", OrderTests[Sum[Int]].order)
  checkAll("Sum", CommutativeMonoidTests[Sum[Int]].commutativeMonoid)
  checkAll("Sum", CommutativeMonadTests[Sum].commutativeMonad[Int, Int, Int])
  checkAll(
    "Sum",
    NonEmptyTraverseTests[Sum]
      .nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option]
  )
  checkAll(
    "Sum",
    DistributiveTests[Sum].distributive[Int, Int, Int, Option, Id]
  )

  test("show") {
    assertEquals(Sum(true).show, "Sum(true)")
    assertEquals(Sum(false).show, "Sum(false)")
  }

  test("addition") {
    assertEquals(List(1, 2, 3, 6).foldMap(Sum(_)), Sum(12))
  }

  test("sum") {
    assertEquals(Sum.sum(List(1, 2, 3, 6)), 12)
  }
}
