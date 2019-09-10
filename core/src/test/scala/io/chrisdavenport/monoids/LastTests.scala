package io.chrisdavenport.monoids

// import cats._
// import cats.implicits._
import cats.tests.CatsSuite
import cats.kernel.laws.discipline._
import cats.laws.discipline._

class LastTests extends CatsSuite with MonoidsArbitraries {
  checkAll("Last", OrderTests[Last[Int]].order)
  checkAll("Last", MonoidTests[Last[Int]].monoid)
  checkAll("Last", MonadTests[Last].monad[Int, Int, Int])
  checkAll("Last", AlternativeTests[Last].alternative[Int, Int, Int])
  checkAll("Last", TraverseTests[Last].traverse[Int, Int, Int, Int,Option, Option])

  test("show"){
    Last(Some(1)).show shouldEqual "Last(Some(1))"
  }

  test("Last precedence"){
    Last(Some(1)) |+| Last(Some(2)) shouldEqual Last(Some(2))
    Last(Option.empty[Int]) |+| Last(Some(2)) shouldEqual Last(Some(2))
    Last(Some(1)) |+| Last(Option.empty[Int]) shouldEqual Last(Some(1))
  }
}
