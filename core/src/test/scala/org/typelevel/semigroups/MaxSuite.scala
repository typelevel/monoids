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

import cats._
import cats.kernel.laws.discipline._
import cats.laws.discipline._
import cats.syntax.all._

class MaxSuite extends munit.DisciplineSuite with SemigroupsArbitraries {
  checkAll("Max", OrderTests[Max[Int]].order)
  checkAll("Max", BoundedSemilatticeTests[Max[Int]].boundedSemilattice)
  checkAll("Max", MonadTests[Max].monad[Int, Int, Int])
  checkAll(
    "Max",
    NonEmptyTraverseTests[Max].nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option]
  )
  checkAll("Max", DistributiveTests[Max].distributive[Int, Int, Int, Option, Id])

  test("show") {
    assertEquals(Max(true).show, "Max(true)")
    assertEquals(Max(false).show, "Max(false)")
  }

  test("returns Larger") {
    val first = Max(1)
    val second = Max(3)
    assertEquals(first |+| second, second)
  }
}
