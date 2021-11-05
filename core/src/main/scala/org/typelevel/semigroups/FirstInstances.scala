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

private[semigroups] trait FirstInstances extends FirstInstances1 {
  implicit def firstBand[A]: Band[First[A]] = new Band[First[A]] {
    def combine(x: First[A], y: First[A]): First[A] = x
  }

  implicit def firstShow[A: Show]: Show[First[A]] =
    Show.show[First[A]](firstA => s"First(${firstA.getFirst.show})")

  implicit def firstOrder[A: Order]: Order[First[A]] =
    Order.by(_.getFirst)

  implicit val firstInstances: CommutativeMonad[First]
    with NonEmptyTraverse[First]
    with Distributive[First] =
    new CommutativeMonad[First] with NonEmptyTraverse[First] with Distributive[First] {
      def pure[A](a: A): First[A] = First(a)
      def flatMap[A, B](fa: First[A])(f: A => First[B]): First[B] = f(fa.getFirst)

      @scala.annotation.tailrec
      def tailRecM[A, B](a: A)(f: A => First[Either[A, B]]): First[B] =
        f(a).getFirst match {
          case Left(a) => tailRecM(a)(f)
          case Right(b) => First(b)
        }
      // Members declared in cats.Foldable
      def foldLeft[A, B](fa: First[A], b: B)(f: (B, A) => B): B =
        f(b, fa.getFirst)
      def foldRight[A, B](fa: First[A], lb: cats.Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        f(fa.getFirst, lb)

      // Members declared in cats.NonEmptyTraverse
      def nonEmptyTraverse[G[_]: Apply, A, B](fa: First[A])(f: A => G[B]): G[First[B]] =
        f(fa.getFirst).map(First(_))

      // Members declared in cats.Reducible
      def reduceLeftTo[A, B](fa: First[A])(f: A => B)(g: (B, A) => B): B =
        f(fa.getFirst)
      def reduceRightTo[A, B](fa: First[A])(f: A => B)(g: (A, Eval[B]) => Eval[B]): Eval[B] =
        f(fa.getFirst).pure[Eval]

      def distribute[G[_]: Functor, A, B](ga: G[A])(f: A => First[B]): First[G[B]] =
        First(ga.map(f).map(_.getFirst))
    }
}

private[semigroups] trait FirstInstances1 {
  implicit def firstEq[A: Eq]: Eq[First[A]] = Eq.by(_.getFirst)
}
