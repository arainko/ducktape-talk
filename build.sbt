ThisBuild / scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value)
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

lazy val http =
  project
    .in(file("http"))
    .settings(Settings.common: _*)
    .settings(
      Compile / guardrailTasks := List(
        ScalaServer(
          specPath  = file("api/seriousBusinessApi.yaml"),
          pkg       = "io.gitbug.arainko.talk.generated",
          framework = "http4s"
        )
      )
    )
    .settings(libraryDependencies += Dependencies.ducktape)
    .settings(libraryDependencies += Dependencies.refined)
    .settings(libraryDependencies += Dependencies.http4sCore)
    .settings(libraryDependencies += Dependencies.http4sCirce)
    .settings(libraryDependencies += Dependencies.http4sDsl)
    .settings(libraryDependencies += Dependencies.http4sEmberClient)
    .settings(libraryDependencies += Dependencies.http4sEmberServer)
    .settings(name := "ducktape-talk")
    .dependsOn(newtypes)

lazy val newtypes =
  project
    .in(file("libs/newtypes"))
    .settings(Settings.common: _*)
    .settings(libraryDependencies += Dependencies.ducktape)
    .settings(libraryDependencies += Dependencies.refined)
    .settings(libraryDependencies += Dependencies.sourcecode)

lazy val root =
  project
    .in(file("."))
    .aggregate(http)
