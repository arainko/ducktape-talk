package io.github.arainko.talk

import smithy4s.Refinement
import java.time.LocalDate
import smithy4s.Surjection
import smithy4s.ShapeTag
import alloy.DateFormat
import smithy4s.RefinementProvider

given dateFormatProvider: RefinementProvider[DateFormat, String, LocalDate] = 
  Refinement.drivenBy[DateFormat](Surjection.catching[String, LocalDate](LocalDate.parse, _.toString()))

