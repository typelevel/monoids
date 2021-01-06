package io.chrisdavenport.monoids.compiling

import cats.implicits._
import io.chrisdavenport.monoids._

object Compilecheck {

  val dualOrder = Dual(1) > Dual(2)
  val dualEq = Dual(1) === Dual(1)
  val dualShow = show"${Dual(1)}"
  val dualMonad = 1.pure[Dual].flatMap(_ => "".pure[Dual])

  val firstMonad = 1.pure[First]
  val firstTraverse = 1.pure[First].traverse(List(_))
  val firstFunctor = 1.pure[First].map(_ + 1)

  val sum = List(1, 2, 3, 4).foldMap(_.pure[Sum])
  val monoidKFirst = First(1.some) <+> First(None)

  val all: Boolean = List(true, false, true).foldMap(Any(_)).getAny
}
