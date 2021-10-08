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
import cats.syntax.all._

private[monoids] trait LastInstances extends LastInstances1 {
  implicit def lastMonoid[A]: Monoid[Last[A]] = MonoidK[Last].algebra

  implicit def lastShow[A: Show]: Show[Last[A]] =
    Show.show[Last[A]](LastA => s"Last(${LastA.getLast.show})")

  implicit def lastOrder[A: Order]: Order[Last[A]] =
    Order.by(_.getLast)

  implicit val lastInstances: Monad[Last] with Traverse[Last] with Alternative[Last] =
    new Monad[Last] with Traverse[Last] with Alternative[Last] {
      def pure[A](a: A): Last[A] = Last(Some(a))
      def flatMap[A, B](fa: Last[A])(f: A => Last[B]): Last[B] =
        Last(fa.getLast.flatMap(f.andThen(_.getLast)))

      @scala.annotation.tailrec
      def tailRecM[A, B](a: A)(f: A => Last[Either[A, B]]): Last[B] =
        f(a).getLast match {
          case Some(Left(a)) => tailRecM(a)(f)
          case Some(Right(b)) => Last(b.some)
          case None => Last(None)
        }
      def foldLeft[A, B](fa: Last[A], b: B)(f: (B, A) => B): B =
        fa.getLast.foldLeft(b)(f)
      def foldRight[A, B](fa: Last[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.getLast.foldRight(lb)(f)

      def traverse[G[_]: Applicative, A, B](fa: Last[A])(f: A => G[B]): G[Last[B]] =
        fa.getLast.traverse(f).map(Last(_))

      // Members declared in cats.MonoidK
      def empty[A]: Last[A] = Last(Option.empty[A])

      // Members declared in cats.SemigroupK
      def combineK[A](x: Last[A], y: Last[A]): Last[A] = Last(y.getLast.orElse(x.getLast))
    }
}

private[monoids] trait LastInstances1 {
  implicit def LastEq[A: Eq]: Eq[Last[A]] = Eq.by(_.getLast)
}
