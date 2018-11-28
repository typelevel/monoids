package io.chrisdavenport.monoids

import cats._
import cats.implicits._
final case class Sum[A](getSum: A) extends AnyVal
object Sum extends SumInstances

private[monoids] trait SumInstances extends SumInstances1 {

  implicit def sumNumeric[A](implicit T: Numeric[A]): Monoid[Sum[A]] = new Monoid[Sum[A]]{
    def empty : Sum[A] = Sum(T.zero)
    def combine(x: Sum[A], y: Sum[A]):Sum[A]= Sum(T.plus(x.getSum, y.getSum))
  }

  implicit def SumShow[A: Show]: Show[Sum[A]] = 
    Show.show[Sum[A]](SumA => show"Sum(${SumA.getSum})")

  implicit def SumOrder[A: Order]: Order[Sum[A]] =
    Order.by(_.getSum)
  implicit val SumMonad: Monad[Sum] = new Monad[Sum]{
    def pure[A](a: A): Sum[A] = Sum(a)
    def flatMap[A,B](fa: Sum[A])(f: A => Sum[B]): Sum[B] = f(fa.getSum)

    @scala.annotation.tailrec
    def tailRecM[A, B](a: A)(f: A => Sum[Either[A,B]]): Sum[B] =
      f(a) match {
        case Sum(Left(a)) => tailRecM(a)(f)
        case Sum(Right(b)) => Sum(b)
      }
  }
}

private[monoids] trait SumInstances1 {
  implicit def SumEq[A: Eq]: Eq[Sum[A]] = Eq.by(_.getSum)
}
