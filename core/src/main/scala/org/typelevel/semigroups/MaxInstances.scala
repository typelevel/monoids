package org.typelevel.semigroups

import cats._
import cats.syntax.all._
import cats.kernel.Semilattice
import cats.kernel.LowerBounded
import cats.kernel.BoundedSemilattice

private[semigroups] trait MaxInstances extends MaxInstances1 {
  def max[F[_]: Reducible, A](fa: F[A])(implicit L: Semigroup[Max[A]]): A =
    fa.reduceMap(Max(_)).getMax

  implicit def orderedMaxBoundedSemilattice[A: LowerBounded: Order]: BoundedSemilattice[Max[A]] =
    new BoundedSemilattice[Max[A]] {
      def combine(x: Max[A], y: Max[A]): Max[A] = Max(Order[A].max(x.getMax, y.getMax))
      def empty: Max[A] = Max(LowerBounded[A].minBound)
    }
}

private[semigroups] trait MaxInstances1 {
  implicit def maxShow[A: Show]: Show[Max[A]] =
    Show.show[Max[A]](maxA => s"Max(${maxA.getMax.show})")

  implicit def maxOrder[A: Order]: Order[Max[A]] =
    Order.by(_.getMax)

  implicit def orderedMaxSemilattice[A: Order]: Semilattice[Max[A]] = new Semilattice[Max[A]] {
    def combine(x: Max[A], y: Max[A]): Max[A] = Max(Order[A].max(x.getMax, y.getMax))
  }

  implicit val maxInstances: CommutativeMonad[Max]
    with NonEmptyTraverse[Max]
    with Distributive[Max] =
    new CommutativeMonad[Max] with NonEmptyTraverse[Max] with Distributive[Max] {
      def pure[A](a: A): Max[A] = Max(a)
      def flatMap[A, B](fa: Max[A])(f: A => Max[B]): Max[B] = f(fa.getMax)

      @scala.annotation.tailrec
      def tailRecM[A, B](a: A)(f: A => Max[Either[A, B]]): Max[B] =
        f(a).getMax match {
          case Left(a) => tailRecM(a)(f)
          case Right(b) => Max(b)
        }
      // Members declared in cats.Foldable
      def foldLeft[A, B](fa: Max[A], b: B)(f: (B, A) => B): B =
        f(b, fa.getMax)
      def foldRight[A, B](fa: Max[A], lb: cats.Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        f(fa.getMax, lb)

      // Members declared in cats.NonEmptyTraverse
      def nonEmptyTraverse[G[_]: Apply, A, B](fa: Max[A])(f: A => G[B]): G[Max[B]] =
        f(fa.getMax).map(Max(_))

      // Members declared in cats.Reducible
      def reduceLeftTo[A, B](fa: Max[A])(f: A => B)(g: (B, A) => B): B =
        f(fa.getMax)
      def reduceRightTo[A, B](fa: Max[A])(f: A => B)(g: (A, Eval[B]) => Eval[B]): Eval[B] =
        f(fa.getMax).pure[Eval]

      def distribute[G[_]: Functor, A, B](ga: G[A])(f: A => Max[B]): Max[G[B]] =
        Max(ga.map(f).map(_.getMax))
    }
}

private[semigroups] trait MaxInstances2 {
  implicit def maxEq[A: Eq]: Eq[Max[A]] = Eq.by(_.getMax)
}
