package io.chrisdavenport.monoids

import cats._
import cats.implicits._
final case class Sum[A](getSum: A) extends AnyVal
object Sum extends SumInstances

private[monoids] trait SumInstances extends SumInstances1 {

  implicit def sumNumericMonoid[A](implicit T: Numeric[A]): Monoid[Sum[A]] = new Monoid[Sum[A]]{
    def empty : Sum[A] = Sum(T.zero)
    def combine(x: Sum[A], y: Sum[A]):Sum[A]= Sum(T.plus(x.getSum, y.getSum))
  }
  implicit def sumNumeric[A](implicit T: Numeric[A]): Numeric[Sum[A]] = new Numeric[Sum[A]]{
    def compare(x: Sum[A], y: Sum[A]): Int = T.compare(x.getSum, y.getSum)
    def fromInt(x: Int): Sum[A] = Sum(T.fromInt(x))
    def minus(x: Sum[A], y: Sum[A]):Sum[A] = Sum(T.minus(x.getSum, y.getSum))
    def negate(x: Sum[A]): Sum[A] = Sum(T.negate(x.getSum))
    def plus(x: Sum[A], y: Sum[A]): Sum[A] = Sum(T.plus(x.getSum, y.getSum))
    def times(x: Sum[A], y: Sum[A]): Sum[A] = Sum(T.times(x.getSum, y.getSum))
    def toDouble(x: Sum[A]): Double = T.toDouble(x.getSum)
    def toFloat(x: Sum[A]): Float = T.toFloat(x.getSum)
    def toInt(x: Sum[A]): Int = T.toInt(x.getSum)
    def toLong(x: Sum[A]): Long  = T.toLong(x.getSum)
  }

  implicit def sumShow[A: Show]: Show[Sum[A]] = 
    Show.show[Sum[A]](SumA => show"Sum(${SumA.getSum})")

  implicit def sumOrder[A: Order]: Order[Sum[A]] =
    Order.by(_.getSum)
  implicit val sumMonad: Monad[Sum] with NonEmptyTraverse[Sum] = new Monad[Sum] with NonEmptyTraverse[Sum] {
    def pure[A](a: A): Sum[A] = Sum(a)
    def flatMap[A,B](fa: Sum[A])(f: A => Sum[B]): Sum[B] = f(fa.getSum)

    @scala.annotation.tailrec
    def tailRecM[A, B](a: A)(f: A => Sum[Either[A,B]]): Sum[B] =
      f(a) match {
        case Sum(Left(a)) => tailRecM(a)(f)
        case Sum(Right(b)) => Sum(b)
      }

    // Members declared in cats.Foldable
    def foldLeft[A, B](fa: Sum[A],b: B)(f: (B, A) => B): B = 
      f(b, fa.getSum)
    def foldRight[A, B](fa: Sum[A],lb: cats.Eval[B])(f: (A, cats.Eval[B]) => cats.Eval[B]): cats.Eval[B] =
      f(fa.getSum, lb)
    
    // Members declared in cats.NonEmptyTraverse
    def nonEmptyTraverse[G[_]: Apply, A, B](fa: Sum[A])(f: A => G[B]): G[Sum[B]] = 
      f(fa.getSum).map(Sum(_))
    
    // Members declared in cats.Reducible
    def reduceLeftTo[A, B](fa: Sum[A])(f: A => B)(g: (B, A) => B): B = 
      f(fa.getSum)
    def reduceRightTo[A, B](fa: Sum[A])(f: A => B)(g: (A, cats.Eval[B]) => cats.Eval[B]): cats.Eval[B] = 
      f(fa.getSum).pure[Eval]
  }
}

private[monoids] trait SumInstances1 {
  implicit def SumEq[A: Eq]: Eq[Sum[A]] = Eq.by(_.getSum)
}
