val commonSettings = Seq(
  name := s"mortgage-calculator-${name.value}",
  description := "Simple mortgage calculator.",
  organization := "net.rouly",
  scalaVersion := "2.13.8",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % "test",
)

ThisBuild / dynverSeparator := "-"

lazy val core = project.settings(commonSettings)

val AkkaVersion = "2.6.18"
val AkkaHttpVersion = "10.2.9"

lazy val http = project
  .enablePlugins(DockerPlugin, JavaServerAppPackaging)
  .dependsOn(core)
  .settings(commonSettings)
  .settings(dockerBaseImage := "openjdk:11-jre")
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,

      "com.lihaoyi" %% "scalatags" % "0.11.1",
      "ch.qos.logback" % "logback-classic" % "1.2.10",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
      "com.github.pureconfig" %% "pureconfig" % "0.17.1"
    )
  )
