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

  val common = List(
    scalaVersion := "3.2.2",
    tpolecatExcludeOptions += ScalacOptions.privateKindProjector,
    tpolecatScalacOptions ++= Set(underscoreTypeLambdas)
  )
}
