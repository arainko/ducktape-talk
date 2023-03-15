ThisBuild / scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value)
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

lazy val http =
  project
    .in(file("http"))
    .settings(Settings.common: _*)
    .settings(libraryDependencies += Dependencies.ducktape)
    .settings(name := "ducktape-talk")

lazy val root =
  project
    .in(file("."))
    .aggregate(http)
