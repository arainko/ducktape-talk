import sbt._

object Dependencies {
  lazy val ducktape          = "io.github.arainko" %% "ducktape"            % "0.1.3+3-db136684-SNAPSHOT"
  lazy val refined           = "eu.timepit"        %% "refined"             % "0.10.2"
  lazy val sourcecode        = "com.lihaoyi"       %% "sourcecode"          % "0.3.0"
  lazy val http4sCore        = "org.http4s"        %% "http4s-core"         % "0.23.18"
  lazy val http4sCirce       = "org.http4s"        %% "http4s-circe"        % "0.23.18"
  lazy val http4sDsl         = "org.http4s"        %% "http4s-dsl"          % "0.23.18"
  lazy val http4sEmberClient = "org.http4s"        %% "http4s-ember-client" % "0.23.18"
  lazy val http4sEmberServer = "org.http4s"        %% "http4s-ember-server" % "0.23.18"
}
