import dev.guardrail.sbt.CodingConfig
ThisBuild / scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value)
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

lazy val httpGuardrail =
  project
    .in(file("http/guardrail"))
    .settings(Settings.common)
    .settings(
      Compile / guardrailTasks := List(
        ScalaServer(
          specPath  = file("api/seriousBusinessApi.yaml"),
          pkg       = "io.github.arainko.talk.generated",
          dto       = "API",
          framework = "http4s"
        )
      )
    )
    .settings(libraryDependencies += Dependencies.http4sCore)
    .settings(libraryDependencies += Dependencies.http4sCirce)
    .settings(libraryDependencies += Dependencies.http4sDsl)
    .settings(libraryDependencies += Dependencies.http4sEmberClient)
    .settings(libraryDependencies += Dependencies.http4sEmberServer)
    .settings(libraryDependencies += Dependencies.log4cats)
    .settings(libraryDependencies += Dependencies.logback)
    .settings(run / fork := true)
    .settings(name := "ducktape-talk-guardrail")
    .dependsOn(infrastructure)

lazy val httpSmithy =
  project
    .in(file("http/smithy"))
    .settings(Settings.common)
    .enablePlugins(Smithy4sCodegenPlugin)
    .settings(
      libraryDependencies ++= Seq(
        Dependencies.http4sEmberServer,
        "com.disneystreaming.smithy4s" %% "smithy4s-http4s"         % smithy4sVersion.value,
        "com.disneystreaming.smithy4s" %% "smithy4s-http4s-swagger" % smithy4sVersion.value,
      )
    )

lazy val domain =
  project
    .in(file("modules/domain"))
    .settings(Settings.common)
    .settings(libraryDependencies += Dependencies.catsEffect)
    .dependsOn(newtypes)

lazy val infrastructure =
  project
    .in(file("modules/infrastructure"))
    .settings(Settings.common)
    .settings(libraryDependencies += Dependencies.monocle)
    .settings(libraryDependencies += Dependencies.log4cats)
    .dependsOn(domain)

lazy val newtypes =
  project
    .in(file("libs/newtypes"))
    .settings(Settings.common)
    .settings(libraryDependencies += Dependencies.ducktape)
    .settings(libraryDependencies += Dependencies.refined)
    .settings(libraryDependencies += Dependencies.sourcecode)

lazy val root =
  project
    .in(file("."))
    .aggregate(httpGuardrail, httpGuardrail)
