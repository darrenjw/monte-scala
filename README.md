# monte-scala
Tools for the development of Monte Carlo algorithms in Scala, including pure Monte Carlo, Approximate Bayesian computation (ABC), Sequential Monte Carlo (SMC) and Markov chain Monte Carlo (MCMC).

**This library is under development - it is not yet ready for general use**

In the meantime, take a look at [my blog](https://darrenjw.wordpress.com/), my [Scala course](https://github.com/darrenjw/scala-course/blob/master/README.md) or my [scala-glm](https://github.com/darrenjw/scala-glm/blob/master/README.md) library for basic regression modelling in Scala.

### Latest snapshot

This library is simplest to use with [SBT](http://www.scala-sbt.org/). You should install SBT before attempting to use this library.

If you want to use the latest snapshot, add the following to your `build.sbt`:

```scala
libraryDependencies += "com.github.darrenjw" %% "monte-scala" % "0.1-SNAPSHOT"
resolvers += "Newcastle mvn repo" at "https://www.staff.ncl.ac.uk/d.j.wilkinson/mvn/"
```

### Building from source

If building from source, running `sbt console` from this directory should give a Scala REPL with a dependence on the library. Running `sbt test` will run all tests. Running `sbt doc` will generate ScalaDoc API documentation.

## Documentation

None as yet...

## Author

This library is Copyright (C) 2017 [Darren J Wilkinson](https://github.com/darrenjw), but released as open source software under an Apache 2.0 license.




