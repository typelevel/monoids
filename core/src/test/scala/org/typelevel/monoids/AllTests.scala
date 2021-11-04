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

import cats.implicits._
import cats.kernel.laws.discipline._

class AllTests extends munit.DisciplineSuite with MonoidsArbitraries {

  checkAll("All", OrderTests[All].order)
  checkAll("All", BoundedSemilatticeTests[All].boundedSemilattice)

  test("show") {
    assertEquals(All(true).show, "All(true)")
    assertEquals(All(false).show, "All(false)")
  }

  test("Any false is false") {
    assertEquals(List(true, true, true).foldMap(All(_)), All(true))
    assertEquals(List(true, false, true).foldMap(All(_)), All(false))
  }

  test("All.all for true, false, true") {
    assertEquals(All.all(List(true, false, true).map(All(_))), All(false))
  }

  test("All.all for true, true, true") {
    assertEquals(All.all(List(true, true, true).map(All(_))), All(true))
  }

  test("All.all for false, false") {
    assertEquals(All.all(List(false, false).map(All(_))), All(false))
  }
}
