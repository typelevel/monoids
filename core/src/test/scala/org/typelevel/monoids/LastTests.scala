package org.typelevel.monoids

import cats.implicits._
import cats.kernel.laws.discipline._
import cats.laws.discipline._

class LastTests extends munit.DisciplineSuite with MonoidsArbitraries {
  checkAll("Last", OrderTests[Last[Int]].order)
  checkAll("Last", MonoidTests[Last[Int]].monoid)
  checkAll("Last", MonadTests[Last].monad[Int, Int, Int])
  checkAll("Last", AlternativeTests[Last].alternative[Int, Int, Int])
  checkAll(
    "Last",
    TraverseTests[Last].traverse[Int, Int, Int, Int, Option, Option]
  )

  test("show") {
    assertEquals(Last(Some(1)).show, "Last(Some(1))")
  }

  test("Last precedence") {
    assertEquals(Last(Some(1)) |+| Last(Some(2)), Last(Some(2)))
    assertEquals(Last(Option.empty[Int]) |+| Last(Some(2)), Last(Some(2)))
    assertEquals(Last(Some(1)) |+| Last(Option.empty[Int]), Last(Some(1)))
  }
}
