import io.github.davidgregory084._
import io.github.davidgregory084.TpolecatPlugin.autoImport._
import sbt._
import sbt.Keys._
import dev.guardrail
import _root_.io.github.davidgregory084
import scala.Ordering.Implicits.*

object Settings {
  private val underscoreTypeLambdas = 
    ScalacOptions.privateOption("kind-projector:underscores", _ >= davidgregory084.ScalaVersion.V3_0_0)

  private val unusedAll = ScalacOptions.warnUnusedOption("all")

  val common = Def.settings(
    scalaVersion := "3.3.1",
    resolvers ++= Resolver.sonatypeOssRepos("snapshots"),
    tpolecatExcludeOptions ++= Set(ScalacOptions.privateKindProjector),
    tpolecatScalacOptions ++= Set(underscoreTypeLambdas)
  )
}
