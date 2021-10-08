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

package org.typelevel.monoids

import cats._
import cats.kernel.CommutativeMonoid
import cats.syntax.all._

private[monoids] trait SumInstances extends SumInstances1 {
  def sum[F[_]: Foldable, A](fa: F[A])(implicit A: Monoid[Sum[A]]): A = fa.foldMap(Sum(_)).getSum

  implicit def sumNumericMonoid[A](implicit T: Numeric[A]): CommutativeMonoid[Sum[A]] =
    new CommutativeMonoid[Sum[A]] {
      def empty: Sum[A] = Sum(T.zero)
      def combine(x: Sum[A], y: Sum[A]): Sum[A] = Sum(T.plus(x.getSum, y.getSum))
    }

  implicit def sumShow[A: Show]: Show[Sum[A]] =
    Show.show[Sum[A]](SumA => s"Sum(${SumA.getSum.show})")

  implicit def sumOrder[A: Order]: Order[Sum[A]] =
    Order.by(_.getSum)

  implicit val sumInstances: CommutativeMonad[Sum]
    with NonEmptyTraverse[Sum]
    with Distributive[Sum] =
    new CommutativeMonad[Sum] with NonEmptyTraverse[Sum] with Distributive[Sum] {
      def pure[A](a: A): Sum[A] = Sum(a)
      def flatMap[A, B](fa: Sum[A])(f: A => Sum[B]): Sum[B] = f(fa.getSum)

      @scala.annotation.tailrec
      def tailRecM[A, B](a: A)(f: A => Sum[Either[A, B]]): Sum[B] =
        f(a).getSum match {
          case Left(a) => tailRecM(a)(f)
          case Right(b) => Sum(b)
        }

      // Members declared in cats.Foldable
      def foldLeft[A, B](fa: Sum[A], b: B)(f: (B, A) => B): B =
        f(b, fa.getSum)
      def foldRight[A, B](fa: Sum[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        f(fa.getSum, lb)

      // Members declared in cats.NonEmptyTraverse
      def nonEmptyTraverse[G[_]: Apply, A, B](fa: Sum[A])(f: A => G[B]): G[Sum[B]] =
        f(fa.getSum).map(Sum(_))

      // Members declared in cats.Reducible
      def reduceLeftTo[A, B](fa: Sum[A])(f: A => B)(g: (B, A) => B): B =
        f(fa.getSum)
      def reduceRightTo[A, B](fa: Sum[A])(f: A => B)(g: (A, Eval[B]) => Eval[B]): Eval[B] =
        f(fa.getSum).pure[Eval]

      def distribute[G[_]: Functor, A, B](ga: G[A])(f: A => Sum[B]): Sum[G[B]] =
        Sum(ga.map(f).map(_.getSum))
    }
}

private[monoids] trait SumInstances1 {
  implicit def SumEq[A: Eq]: Eq[Sum[A]] = Eq.by(_.getSum)
}
