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

private[monoids] trait FirstInstances extends FirstInstances1 {
  implicit def firstMonoid[A]: Monoid[First[A]] = MonoidK[First].algebra

  implicit def firstShow[A: Show]: Show[First[A]] =
    Show.show[First[A]](FirstA => s"First(${FirstA.getFirst.show})")

  implicit def firstOrder[A: Order]: Order[First[A]] =
    Order.by(_.getFirst)

  implicit val firstInstances: Monad[First] with Traverse[First] with Alternative[First] =
    new Monad[First] with Traverse[First] with Alternative[First] {
      def pure[A](a: A): First[A] = First(Some(a))
      def flatMap[A, B](fa: First[A])(f: A => First[B]): First[B] =
        First(fa.getFirst.flatMap(f.andThen(_.getFirst)))

      @scala.annotation.tailrec
      def tailRecM[A, B](a: A)(f: A => First[Either[A, B]]): First[B] =
        f(a).getFirst match {
          case Some(Left(a)) => tailRecM(a)(f)
          case Some(Right(b)) => First(b.some)
          case None => First(None)
        }
      def foldLeft[A, B](fa: First[A], b: B)(f: (B, A) => B): B =
        fa.getFirst.foldLeft(b)(f)
      def foldRight[A, B](fa: First[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.getFirst.foldRight(lb)(f)

      def traverse[G[_]: Applicative, A, B](fa: First[A])(f: A => G[B]): G[First[B]] =
        fa.getFirst.traverse(f).map(First(_))

      def empty[A]: First[A] = First(Option.empty[A])

      // Members declared in cats.SemigroupK
      def combineK[A](x: First[A], y: First[A]): First[A] = First(x.getFirst.orElse(y.getFirst))

    }
}

private[monoids] trait FirstInstances1 {
  implicit def FirstEq[A: Eq]: Eq[First[A]] = Eq.by(_.getFirst)
}
