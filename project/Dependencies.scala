import sbt._

object Dependencies {
  lazy val ducktape          = "io.github.arainko" %% "ducktape"            % "0.2.0-M4"
  lazy val refined           = "eu.timepit"        %% "refined"             % "0.10.2"
  lazy val sourcecode        = "com.lihaoyi"       %% "sourcecode"          % "0.3.0"
  lazy val catsEffect        = "org.typelevel"     %% "cats-effect"         % "3.4.8"
  lazy val log4cats          = "org.typelevel"     %% "log4cats-slf4j"      % "2.5.0"
  lazy val http4sCore        = "org.http4s"        %% "http4s-core"         % "0.23.18"
  lazy val http4sCirce       = "org.http4s"        %% "http4s-circe"        % "0.23.18"
  lazy val http4sDsl         = "org.http4s"        %% "http4s-dsl"          % "0.23.18"
  lazy val http4sEmberClient = "org.http4s"        %% "http4s-ember-client" % "0.23.18"
  lazy val http4sEmberServer = "org.http4s"        %% "http4s-ember-server" % "0.23.18"
  lazy val monocle           = "dev.optics"        %% "monocle-core"        % "3.2.0"
  lazy val logback           = "ch.qos.logback"     % "logback-classic"     % "1.4.6"
}
