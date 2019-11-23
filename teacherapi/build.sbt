name := "tunihack-student-service"

organization := "cognira.tunihack"
version := "1.2.8"

scalaVersion := "2.11.8"

enablePlugins(PackPlugin)

val akkaActorsVersion = "2.5.20"
val akkaStreamsVersion = "2.5.20"
val akkaHttpVersion = "10.1.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaActorsVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaStreamsVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.3.0",
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "io.spray" %% "spray-json" % "1.3.5",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.0.pr2",
  // https://mvnrepository.com/artifact/io.monix/monix
  "io.monix" %% "monix" % "3.0.0-8084549",
  "com.datastax.cassandra" % "cassandra-driver-extras" % "3.1.4",
  "com.typesafe.akka" %% "akka-actor-typed" % akkaActorsVersion
)
