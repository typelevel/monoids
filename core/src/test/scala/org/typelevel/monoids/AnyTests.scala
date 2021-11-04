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

class AnyTests extends munit.DisciplineSuite with MonoidsArbitraries {
  checkAll("Any", OrderTests[Any].order)
  checkAll("Any", BoundedSemilatticeTests[Any].boundedSemilattice)

  test("show") {
    assertEquals(Any(true).show, "Any(true)")
    assertEquals(Any(false).show, "Any(false)")
  }

  test("Any true is true") {
    assertEquals(List(false, false, false).foldMap(Any(_)), Any(false))
    assertEquals(List(false, true, false).foldMap(Any(_)), Any(true))
  }

  test("Any.any for true, false, true") {
    assertEquals(Any.any(List(true, false, true).map(Any(_))), Any(true))
  }

  test("Any.any for true, true, true") {
    assertEquals(Any.any(List(true, true, true).map(Any(_))), Any(true))
  }

  test("Any.any for false, false") {
    assertEquals(Any.any(List(false, false).map(Any(_))), Any(false))
  }

}
