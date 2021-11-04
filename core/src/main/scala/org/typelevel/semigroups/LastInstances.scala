package org.typelevel.semigroups

import cats._
import cats.syntax.all._
import cats.kernel.Band

private[semigroups] trait LastInstances extends LastInstances1 {
  implicit def lastBand[A]: Band[Last[A]] = new Band[Last[A]] {
    def combine(x: Last[A], y: Last[A]): Last[A] = y
  }
}

private[semigroups] trait LastInstances1 {
  implicit def lastShow[A: Show]: Show[Last[A]] =
    Show.show[Last[A]](lastA => s"Last(${lastA.getLast.show})")

  implicit def LastOrder[A: Order]: Order[Last[A]] =
    Order.by(_.getLast)

  implicit val lastInstances: CommutativeMonad[Last]
    with NonEmptyTraverse[Last]
    with Distributive[Last] =
    new CommutativeMonad[Last] with NonEmptyTraverse[Last] with Distributive[Last] {
      def pure[A](a: A): Last[A] = Last(a)
      def flatMap[A, B](fa: Last[A])(f: A => Last[B]): Last[B] = f(fa.getLast)

      @scala.annotation.tailrec
      def tailRecM[A, B](a: A)(f: A => Last[Either[A, B]]): Last[B] =
        f(a).getLast match {
          case Left(a) => tailRecM(a)(f)
          case Right(b) => Last(b)
        }
      // Members declared in cats.Foldable
      def foldLeft[A, B](fa: Last[A], b: B)(f: (B, A) => B): B =
        f(b, fa.getLast)
      def foldRight[A, B](fa: Last[A], lb: cats.Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        f(fa.getLast, lb)

      // Members declared in cats.NonEmptyTraverse
      def nonEmptyTraverse[G[_]: Apply, A, B](fa: Last[A])(f: A => G[B]): G[Last[B]] =
        f(fa.getLast).map(Last(_))

      // Members declared in cats.Reducible
      def reduceLeftTo[A, B](fa: Last[A])(f: A => B)(g: (B, A) => B): B =
        f(fa.getLast)
      def reduceRightTo[A, B](fa: Last[A])(f: A => B)(g: (A, Eval[B]) => Eval[B]): Eval[B] =
        f(fa.getLast).pure[Eval]

      def distribute[G[_]: Functor, A, B](ga: G[A])(f: A => Last[B]): Last[G[B]] =
        Last(ga.map(f).map(_.getLast))
    }
}

private[semigroups] trait LastInstances2 {
  implicit def lastEq[A: Eq]: Eq[Last[A]] = Eq.by(_.getLast)
}
