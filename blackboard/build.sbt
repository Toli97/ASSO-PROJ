import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.asso"
ThisBuild / organizationName := "asso"

// skip tests in assembly plugin command
test in assembly := {}

lazy val root = (project in file("."))
  .settings(
    name := "blackboard",
    libraryDependencies += scalaTest % Test
  )