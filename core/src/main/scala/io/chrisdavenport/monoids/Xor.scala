package io.chrisdavenport.monoids

import cats._
import cats.kernel.{CommutativeGroup, CommutativeMonoid}
import cats.implicits._

import scala.annotation.nowarn

final case class Xor[A](getXor: Set[A]) extends AnyVal
object Xor extends XorInstances

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
