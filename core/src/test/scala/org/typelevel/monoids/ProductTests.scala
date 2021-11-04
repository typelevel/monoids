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

class ProductTests extends munit.DisciplineSuite with MonoidsArbitraries {
  checkAll("Product", EqTests[Product[Int]].eqv)
  checkAll("Product", OrderTests[Product[Int]].order)
  checkAll("Product", CommutativeMonoidTests[Product[Int]].commutativeMonoid)
  checkAll(
    "Product",
    CommutativeMonadTests[Product].commutativeMonad[Int, Int, Int]
  )
  checkAll(
    "Product",
    NonEmptyTraverseTests[Product]
      .nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option]
  )
  checkAll(
    "Product",
    DistributiveTests[Product].distributive[Int, Int, Int, Option, Id]
  )

  test("show") {
    assertEquals(Product(true).show, "Product(true)")
    assertEquals(Product(false).show, "Product(false)")
  }

  test("multiplication") {
    assertEquals(List(1, 2, 3, 6).foldMap(Product(_)), Product(36))
  }

  test("product") {
    assertEquals(Product.product(List(1, 2, 3, 6)), 36)
  }
}
