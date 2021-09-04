---
layout: home

---
# monoids ![Continuous Integration](https://github.com/typelevel/monoids/workflows/Continuous%20Integration/badge.svg) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/monoids_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/monoids_2.13)

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

To use this project in an existing SBT project with Scala 2.11 or a later version, 
add the following dependencies to your `build.sbt` depending on your needs:

```scala
libraryDependencies ++= Seq(
  "io.chrisdavenport" %% "monoids" % "<version>"
)
```

## Examples

First some imports.

```tut:silent
import cats._
import cats.implicits._
import io.chrisdavenport.monoids._
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
