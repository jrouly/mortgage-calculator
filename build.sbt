name := "mortgage-calculator"
description := "Simple mortgage calculator."
organization := "net.rouly"
scalaVersion := "3.1.1"

Global / onChangedBuildSource := ReloadOnSourceChanges

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % "test"
