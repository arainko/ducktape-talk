ThisBuild / scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value)
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

lazy val http =
  project
    .in(file("http"))
    .settings(Settings.common: _*)
    .settings(libraryDependencies += Dependencies.ducktape)
    .settings(libraryDependencies += Dependencies.refined)
    .settings(name := "ducktape-talk")
    .dependsOn(newtypes)

lazy val newtypes =
  project
    .in(file("libs/newtypes"))
    .settings(Settings.common: _*)
    .settings(libraryDependencies += Dependencies.ducktape)
    .settings(libraryDependencies += Dependencies.refined)

lazy val root =
  project
    .in(file("."))
    .aggregate(http)
