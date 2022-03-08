val commonSettings = Seq(
  name := s"mortgage-calculator-${name.value}",
  description := "Simple mortgage calculator.",
  organization := "net.rouly",
  scalaVersion := "2.13.8",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % "test"
)

lazy val core = project.settings(commonSettings)

val AkkaVersion = "2.6.18"
val AkkaHttpVersion = "10.2.9"

lazy val http = project
  .dependsOn(core)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,

      "ch.qos.logback" % "logback-classic" % "1.2.10",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
      "com.github.pureconfig" %% "pureconfig" % "0.17.1"
    )
  )
