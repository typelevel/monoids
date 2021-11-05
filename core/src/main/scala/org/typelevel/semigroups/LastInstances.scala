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
