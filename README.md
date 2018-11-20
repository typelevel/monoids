# monoids [![Build Status](https://travis-ci.com/ChristopherDavenport/monoids.svg?branch=master)](https://travis-ci.com/ChristopherDavenport/monoids) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/monoids_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/monoids_2.12)


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

To use this project in an existing SBT project with Scala 2.11 or a later version, add the following dependencies to your
`build.sbt` depending on your needs:

```scala
libraryDependencies ++= Seq(
  "io.chrisdavenport" %% "monoids"     % "<version>"
)
```
