name := "monte-scala"
organization := "com.github.darrenjw"
version := "0.1-SNAPSHOT"

scalacOptions ++= Seq(
  "-unchecked", "-deprecation", "-feature",
  "-language:higherKinds", "-language:implicitConversions"
)

libraryDependencies  ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.typelevel" %% "cats-core" % "1.0.1",
  "com.github.mpilquist" %% "simulacrum" % "0.10.0",
  "com.github.darrenjw" %% "scala-glm" % "0.3"
)

resolvers ++= Seq(
  "Sonatype Snapshots" at
    "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at
    "https://oss.sonatype.org/content/repositories/releases/"
)

scalaVersion := "2.12.3"
crossScalaVersions := Seq("2.11.11","2.12.3")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

publishTo := Some(Resolver.sftp("Personal mvn repo", "unix.ncl.ac.uk", "/home/ucs/100/ndjw1/public_html/mvn"))



// eof


