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
import cats.kernel.{CommutativeMonoid, Semilattice}

private[semigroups] abstract class IntersectInstances {
  implicit def intersectSemilattice[A]: Semilattice[Intersect[A]] = new Semilattice[Intersect[A]] {
    def combine(x: Intersect[A], y: Intersect[A]): Intersect[A] = Intersect(
      x.getIntersect.intersect(y.getIntersect)
    )
  }

  implicit def intersectShow[A: Show]: Show[Intersect[A]] =
    Show.show[Intersect[A]](ia => s"Intersect(${ia.getIntersect.show})")

  implicit def IntersectEq[A]: Eq[Intersect[A]] = Eq.by(_.getIntersect)

  implicit val intersectInstances: MonoidK[Intersect] with UnorderedTraverse[Intersect] =
    new MonoidK[Intersect] with UnorderedTraverse[Intersect] {
      def empty[A]: Intersect[A] = Intersect(MonoidK[Set].empty)
      def combineK[A](x: Intersect[A], y: Intersect[A]): Intersect[A] =
        Intersect(SemigroupK[Set].combineK(x.getIntersect, y.getIntersect))
      def unorderedTraverse[G[_]: CommutativeApplicative, A, B](sa: Intersect[A])(
          f: A => G[B]
      ): G[Intersect[B]] =
        UnorderedTraverse[Set].unorderedTraverse(sa.getIntersect)(f).map(Intersect(_))
      def unorderedFoldMap[A, B: CommutativeMonoid](fa: Intersect[A])(f: A => B): B =
        UnorderedFoldable[Set].unorderedFoldMap(fa.getIntersect)(f)
    }
}
