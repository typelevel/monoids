package io.chrisdavenport.monoids

import cats._
import cats.implicits._

final case class Dual[A](getDual: A) extends AnyVal
object Dual extends DualInstances

private[monoids] trait DualInstances extends DualInstances1 {
  implicit def dualMonoid[A: Monoid]: Monoid[Dual[A]] = new Monoid[Dual[A]]{
    def empty: Dual[A] = Dual(Monoid[A].empty)
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = 
      Dual(Monoid[A].combine(y.getDual, x.getDual))
  }
  implicit def dualShow[A: Show]: Show[Dual[A]] = 
    Show.show[Dual[A]](dualA => show"Dual(${dualA.getDual})")

  implicit def dualOrder[A: Order]: Order[Dual[A]] =
    Order.by(_.getDual)
  implicit val dualMonad: Monad[Dual] = new Monad[Dual]{
    def pure[A](a: A): Dual[A] = Dual(a)
    def flatMap[A,B](fa: Dual[A])(f: A => Dual[B]): Dual[B] = f(fa.getDual)

    @scala.annotation.tailrec
    def tailRecM[A, B](a: A)(f: A => Dual[Either[A,B]]): Dual[B] =
      f(a) match {
        case Dual(Left(a)) => tailRecM(a)(f)
        case Dual(Right(b)) => Dual(b)
      }
  }
}

private[monoids] trait DualInstances1 {
  implicit def dualEq[A: Eq]: Eq[Dual[A]] = Eq.by(_.getDual)
}