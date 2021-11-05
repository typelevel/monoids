# monoids ![Continuous Integration](https://github.com/typelevel/monoids/workflows/Continuous%20Integration/badge.svg) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.typelevel/monoids_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.typelevel/monoids_2.13)

Monoids is a library of a set of generic `Monoid` and `Semigroup` types that are very useful for abstract programming.

This library exposes some generic `Monoid`s:

- `All` - Boolean newtype that combines values using `&&`
- `Any` - Boolean newtype that combines values using `||`
- `Dual` - Inverts the Combine operation of a monoid
- `First` - Option newtype that combine takes the first element that is present
- `Last` - Option newtype that combine takes the last element that is present
- `Product` - Numeric newtype that combines values using Multiplication
- `Sum` - Numeric newtype that combines values using Addition

Also some generic `Semigroup`s:
- `Dual` inverts the combine operation.
- `Max` exposes a Max that given an `Order` will return the maximum value.
- `Min` exposes a Min that given an `Order` will return the minimum value.

## [Head on over to the Microsite](https://typelevel.org/monoids/)

## Quick Start

To use this project in an existing SBT project with Scala 2.12 or a later version, add the following dependencies to your
`build.sbt` depending on your needs:

```scala
libraryDependencies ++= Seq(
  "org.typelevel" %% "monoids" % "<version>"
)
```
