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
import cats.kernel.{CommutativeGroup, CommutativeMonoid}
import cats.syntax.all._

import scala.annotation.nowarn

private[monoids] abstract class XorInstances {
  implicit def xor[A]: CommutativeGroup[Xor[A]] = new CommutativeGroup[Xor[A]] {
    def combine(x: Xor[A], y: Xor[A]): Xor[A] = Xor(
      x.getXor.diff(y.getXor).union(y.getXor.diff(x.getXor))
    )
    def empty: Xor[A] = Xor(Set.empty)
    def inverse(a: Xor[A]): Xor[A] = a
  }

  implicit def xorShow[A: Show]: Show[Xor[A]] =
    Show.show[Xor[A]](xorA => show"Xor(${xorA.getXor})")

  @nowarn
  implicit def xorEq[A: Eq]: Eq[Xor[A]] = Eq.by(_.getXor)

  implicit val xorInstances: MonoidK[Xor] with UnorderedTraverse[Xor] =
    new MonoidK[Xor] with UnorderedTraverse[Xor] {
      def empty[A]: Xor[A] = Xor(MonoidK[Set].empty)
      def combineK[A](x: Xor[A], y: Xor[A]): Xor[A] =
        Xor(SemigroupK[Set].combineK(x.getXor, y.getXor))
      def unorderedTraverse[G[_]: CommutativeApplicative, A, B](sa: Xor[A])(
          f: A => G[B]
      ): G[Xor[B]] =
        UnorderedTraverse[Set].unorderedTraverse(sa.getXor)(f).map(Xor(_))
      def unorderedFoldMap[A, B: CommutativeMonoid](fa: Xor[A])(f: A => B): B =
        UnorderedFoldable[Set].unorderedFoldMap(fa.getXor)(f)
    }
}
