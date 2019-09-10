package io.chrisdavenport.monoids

// import cats._
// import cats.implicits._
import cats.tests.CatsSuite
import cats.kernel.laws.discipline._
import cats.laws.discipline._

class FirstTests extends CatsSuite with MonoidsArbitraries {
  checkAll("First", OrderTests[First[Int]].order)
  checkAll("First", MonoidTests[First[Int]].monoid)
  checkAll("First", MonadTests[First].monad[Int, Int, Int])
  checkAll("First", AlternativeTests[First].alternative[Int, Int, Int])
  checkAll("First", TraverseTests[First].traverse[Int, Int, Int, Int,Option, Option])

  test("show"){
    First(Some(1)).show shouldEqual "First(Some(1))"
  }

  test("first precedence"){
    First(Some(1)) |+| First(Some(2)) shouldEqual First(Some(1))
    First(Option.empty[Int]) |+| First(Some(2)) shouldEqual First(Some(2))
    First(Some(1)) |+| First(Option.empty[Int]) shouldEqual First(Some(1))
  }
}