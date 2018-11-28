package io.chrisdavenport.monoids

import cats._
import cats.implicits._

final case class Last[A](getLast: Option[A]) extends AnyVal
object Last extends LastInstances

private[monoids] trait LastInstances  extends LastInstances1 {
  implicit def lastMonoid[A]: Monoid[Last[A]] = MonoidK[Last].algebra

  implicit def lastShow[A: Show]: Show[Last[A]] = 
    Show.show[Last[A]](LastA => show"Last(${LastA.getLast})")

  implicit def lastOrder[A: Order]: Order[Last[A]] =
    Order.by(_.getLast)

  implicit val lastInstances: Monad[Last] with Traverse[Last] with Alternative[Last] = 
    new Monad[Last] with Traverse[Last] with Alternative[Last] {
      def pure[A](a: A): Last[A] = Last(Some(a))
      def flatMap[A,B](fa: Last[A])(f: A => Last[B]): Last[B] = 
        Last(fa.getLast.flatMap(f.andThen(_.getLast)))
      
      @scala.annotation.tailrec
      def tailRecM[A, B](a: A)(f: A => Last[Either[A,B]]): Last[B] =
        f(a) match {
          case Last(Some(Left(a))) => tailRecM(a)(f)
          case Last(Some(Right(b))) => Last(b.some)
          case Last(None) => Last(None)
        }
      def foldLeft[A, B](fa: Last[A],b: B)(f: (B, A) => B): B = 
        fa.getLast.foldLeft(b)(f)
      def foldRight[A, B](fa: Last[A],lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.getLast.foldRight(lb)(f)
      
      def traverse[G[_]: Applicative, A, B](fa: Last[A])(f: A => G[B]): G[Last[B]] = 
        fa.getLast.traverse(f).map(Last(_))

      // Members declared in cats.MonoidK
      def empty[A]: Last[A] = Last(Option.empty[A])
    
      // Members declared in cats.SemigroupK
      def combineK[A](x: Last[A],y: Last[A]): Last[A] = Last(y.getLast.orElse(x.getLast))
    }
}

private[monoids] trait LastInstances1 {
  implicit def LastEq[A: Eq]: Eq[Last[A]] = Eq.by(_.getLast)
}