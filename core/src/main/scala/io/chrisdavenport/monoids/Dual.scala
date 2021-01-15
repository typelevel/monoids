package io.chrisdavenport.monoids

import cats._
import cats.implicits._

final case class Dual[A](getDual: A)
object Dual extends DualInstances

private[monoids] trait DualInstances extends DualInstances1 {
  implicit def dualMonoid[A: Monoid]: Monoid[Dual[A]] = new Monoid[Dual[A]] {
    val empty: Dual[A] = Dual(Monoid[A].empty)
    def combine(x: Dual[A], y: Dual[A]): Dual[A] =
      Dual(Monoid[A].combine(y.getDual, x.getDual))
  }
  implicit def dualShow[A: Show]: Show[Dual[A]] =
    Show.show[Dual[A]](dualA => show"Dual(${dualA.getDual})")

  implicit def dualOrder[A: Order]: Order[Dual[A]] =
    Order.by(_.getDual)

  implicit val dualInstances: Monad[Dual] with NonEmptyTraverse[Dual] with Distributive[Dual] =
    new Monad[Dual] with NonEmptyTraverse[Dual] with Distributive[Dual] {
      def pure[A](a: A): Dual[A] = Dual(a)
      def flatMap[A, B](fa: Dual[A])(f: A => Dual[B]): Dual[B] = f(fa.getDual)

      @scala.annotation.tailrec
      def tailRecM[A, B](a: A)(f: A => Dual[Either[A, B]]): Dual[B] =
        f(a) match {
          case Dual(Left(a)) => tailRecM(a)(f)
          case Dual(Right(b)) => Dual(b)
        }
      // Members declared in cats.Foldable
      def foldLeft[A, B](fa: Dual[A], b: B)(f: (B, A) => B): B =
        f(b, fa.getDual)
      def foldRight[A, B](fa: Dual[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): cats.Eval[B] =
        f(fa.getDual, lb)

      // Members declared in cats.NonEmptyTraverse
      def nonEmptyTraverse[G[_]: Apply, A, B](fa: Dual[A])(f: A => G[B]): G[Dual[B]] =
        f(fa.getDual).map(Dual(_))

      // Members declared in cats.Reducible
      def reduceLeftTo[A, B](fa: Dual[A])(f: A => B)(g: (B, A) => B): B =
        f(fa.getDual)
      def reduceRightTo[A, B](fa: Dual[A])(f: A => B)(g: (A, Eval[B]) => Eval[B]): Eval[B] =
        f(fa.getDual).pure[Eval]

      def distribute[G[_]: Functor, A, B](ga: G[A])(f: A => Dual[B]): Dual[G[B]] =
        Dual(ga.map(f).map(_.getDual))
    }
}

private[monoids] trait DualInstances1 {
  implicit def dualEq[A: Eq]: Eq[Dual[A]] = Eq.by(_.getDual)
}
