/*
 * Copyright (c) 2021 Typelevel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.typelevel.semigroups

import cats._
import cats.syntax.all._
import cats.kernel.{BoundedSemilattice, Semilattice, UpperBounded}

private[semigroups] trait MinInstances extends MinInstances1 {
  def min[F[_]: Reducible, A](fa: F[A])(implicit L: Semigroup[Min[A]]): A =
    fa.reduceMap(Min(_)).getMin

  implicit def orderedMinBoundedSemilattice[A: UpperBounded: Order]: BoundedSemilattice[Min[A]] =
    new BoundedSemilattice[Min[A]] {
      def combine(x: Min[A], y: Min[A]): Min[A] = Min(Order[A].min(x.getMin, y.getMin))
      def empty: Min[A] = Min(UpperBounded[A].maxBound)
    }
}

private[semigroups] trait MinInstances1 {
  implicit def minShow[A: Show]: Show[Min[A]] =
    Show.show[Min[A]](minA => s"Min(${minA.getMin.show})")

  implicit def minOrder[A: Order]: Order[Min[A]] =
    Order.by(_.getMin)

  implicit def orderedMinSemilattice[A: Order]: Semilattice[Min[A]] = new Semilattice[Min[A]] {
    def combine(x: Min[A], y: Min[A]): Min[A] =
      Min(Order.min(x.getMin, y.getMin))
  }

  implicit val minInstances: CommutativeMonad[Min]
    with NonEmptyTraverse[Min]
    with Distributive[Min] =
    new CommutativeMonad[Min] with NonEmptyTraverse[Min] with Distributive[Min] {
      def pure[A](a: A): Min[A] = Min(a)
      def flatMap[A, B](fa: Min[A])(f: A => Min[B]): Min[B] = f(fa.getMin)

      @scala.annotation.tailrec
      def tailRecM[A, B](a: A)(f: A => Min[Either[A, B]]): Min[B] =
        f(a).getMin match {
          case Left(a) => tailRecM(a)(f)
          case Right(b) => Min(b)
        }
      // Members declared in cats.Foldable
      def foldLeft[A, B](fa: Min[A], b: B)(f: (B, A) => B): B =
        f(b, fa.getMin)
      def foldRight[A, B](fa: Min[A], lb: cats.Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        f(fa.getMin, lb)

      // Members declared in cats.NonEmptyTraverse
      def nonEmptyTraverse[G[_]: Apply, A, B](fa: Min[A])(f: A => G[B]): G[Min[B]] =
        f(fa.getMin).map(Min(_))

      // Members declared in cats.Reducible
      def reduceLeftTo[A, B](fa: Min[A])(f: A => B)(g: (B, A) => B): B =
        f(fa.getMin)
      def reduceRightTo[A, B](fa: Min[A])(f: A => B)(g: (A, Eval[B]) => Eval[B]): Eval[B] =
        f(fa.getMin).pure[Eval]

      def distribute[G[_]: Functor, A, B](ga: G[A])(f: A => Min[B]): Min[G[B]] =
        Min(ga.map(f).map(_.getMin))
    }
}

private[semigroups] trait MinInstances2 {
  implicit def minEq[A: Eq]: Eq[Min[A]] = Eq.by(_.getMin)
}
