---
layout: home

---
# monoids ![Continuous Integration](https://github.com/typelevel/monoids/workflows/Continuous%20Integration/badge.svg) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.typelevel/monoids_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.typelevel/monoids_2.13)

Monoids is a library for some generic Monoids that are very useful.

This library exposes

- `All` - Boolean newtype that combines values using `&&`
- `Any` - Boolean newtype that combines values using `||`
- `Dual` - Inverts the Combine operation of a monoid
- `First` - Option newtype that combine takes the first element that is present
- `Last` - Option newtype that combine takes the last element that is present
- `Product` - Numeric newtype that combines values using Multiplication
- `Sum` - Numeric newtype that combines values using Addition

## Quick Start

To use this project in an existing SBT project with Scala 2.12 or a later version, 
add the following dependencies to your `build.sbt` depending on your needs:

```scala
libraryDependencies ++= Seq(
  "org.typelevel" %% "monoids" % "<version>"
)
```

## Examples

First some imports.

```tut:silent
import cats._
import cats.implicits._
import org.typelevel.monoids._
```

The boolean monoids lift boolean algebras into monoids.

```tut:book
List(true, true, true).foldMap(All(_))

List(true, false, true).foldMap(All(_))

List(false, false, false).foldMap(All(_))

List(false, true, false).foldMap(All(_))
```

`Sum`/`Product` allow explicit composition of Numeric values

```tut:book
List(1,2,3,6).foldMap(Sum(_))
List(1,2,3,6).foldMap(Product(_))
```

`First`/`Last` Option wrappers allow composition based on position and existence.
So `First` takes the first value that is non empty down the chain of composition

```tut:book
First(1.some) |+| First(2.some)
First(1.some) |+| First(Option.empty[Int])
First(Option.empty[Int]) |+| First(2.some)
First(Option.empty[Int]) |+| First(2.some) |+| First(3.some)
First(Option.empty[Int]) |+| First(Option.empty[Int]) |+| First(3.some)

Last(1.some) |+| Last(2.some)
Last(1.some) |+| Last(Option.empty[Int])
Last(Option.empty[Int]) |+| Last(2.some)
Last(1.some) |+| Last(2.some) |+| Last(Option.empty[Int])
Last(1.some) |+| Last(Option.empty[Int]) |+| Last(Option.empty[Int])
```

As mentioned above, `Dual` inverts the `combine` operation of a `Monoid`.
That could be well observed for the non-commutative algebra.

```tut:book
Dual("World") |+| Dual("Hello")
Dual("Hello") |+| Dual("World")

Dual(1) |+| Dual(2)
Dual(2) |+| Dual(1)
```


## Scala 3 support
Scala 3 support implemented using [opaque types](https://docs.scala-lang.org/scala3/book/types-opaque-types.html#opaque-types).
Due to limitation in opaque types `toString()` method on every `monoids` type 
produce unwrapped-value without wrapper-type information, e.g.:
```tut:silent
val resultString = List(true, true, true).foldMap(All(_)).toString // "true"
```
That behaviour differs from Scala 2 one - `toString()` method would produce wrapped-value:
```tut:silent
val resultString = List(true, true, true).foldMap(All(_)).toString // "All(true)"
```
So it's recommended to use `Show[T].show` from `cats` for converting to string. 
Its behaviour is consistent for both Scala 2 and Scala 3:
```tut:silent
val resultString = List(true, true, true).foldMap(All(_)).show // "All(true)"
```
