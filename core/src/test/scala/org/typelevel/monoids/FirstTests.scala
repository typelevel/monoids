package org.typelevel.monoids

import cats.implicits._
import cats.kernel.laws.discipline._
import cats.laws.discipline._

class FirstTests extends munit.DisciplineSuite with MonoidsArbitraries {
  checkAll("First", OrderTests[First[Int]].order)
  checkAll("First", MonoidTests[First[Int]].monoid)
  checkAll("First", MonadTests[First].monad[Int, Int, Int])
  checkAll("First", AlternativeTests[First].alternative[Int, Int, Int])
  checkAll(
    "First",
    TraverseTests[First].traverse[Int, Int, Int, Int, Option, Option]
  )

  test("show") {
    assertEquals(First(Some(1)).show, "First(Some(1))")
  }

  test("first precedence") {
    assertEquals(First(Some(1)) |+| First(Some(2)), First(Some(1)))
    assertEquals(First(Option.empty[Int]) |+| First(Some(2)), First(Some(2)))
    assertEquals(First(Some(1)) |+| First(Option.empty[Int]), First(Some(1)))
  }
}
