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

private[semigroups] trait DualInstances extends DualInstances1 {
  implicit def dualSemigroup[A: Semigroup]: Semigroup[Dual[A]] = new Semigroup[Dual[A]] {
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = Dual(Semigroup[A].combine(y.getDual, x.getDual))
  }
  implicit def dualShow[A: Show]: Show[Dual[A]] =
    Show.show[Dual[A]](dualA => s"Dual(${dualA.getDual.show})")

  implicit def dualOrder[A: Order]: Order[Dual[A]] =
    Order.by(_.getDual)

  implicit val dualInstances: CommutativeMonad[Dual]
    with NonEmptyTraverse[Dual]
    with Distributive[Dual] =
    new CommutativeMonad[Dual] with NonEmptyTraverse[Dual] with Distributive[Dual] {
      def pure[A](a: A): Dual[A] = Dual(a)
      def flatMap[A, B](fa: Dual[A])(f: A => Dual[B]): Dual[B] = f(fa.getDual)

      @scala.annotation.tailrec
      def tailRecM[A, B](a: A)(f: A => Dual[Either[A, B]]): Dual[B] =
        f(a).getDual match {
          case Left(a) => tailRecM(a)(f)
          case Right(b) => Dual(b)
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

private[semigroups] trait DualInstances1 {
  implicit def dualEq[A: Eq]: Eq[Dual[A]] = Eq.by(_.getDual)
}
