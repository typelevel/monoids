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

class LastSuite extends munit.DisciplineSuite with SemigroupsArbitraries {
  checkAll("Last", OrderTests[Last[Int]].order)
  checkAll("Last", BandTests[Last[Int]].band)
  checkAll("Last", MonadTests[Last].monad[Int, Int, Int])
  checkAll(
    "Last",
    NonEmptyTraverseTests[Last].nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option]
  )
  checkAll("Last", DistributiveTests[Last].distributive[Int, Int, Int, Option, Id])

  test("show") {
    assertEquals(Last(true).show, "Last(true)")
    assertEquals(Last(false).show, "Last(false)")
  }

  test("returns Last") {
    val first = Last("Hello ")
    val second = Last("World")
    assertEquals(first |+| second, second)
  }
}
