package io.github.arainko.talk.domain.model

import java.time.LocalDate
import cats.syntax.either.*
import io.github.arainko.newtypes.UnsafeTransformations

final case class DateSpan private (start: LocalDate, end: LocalDate)

object DateSpan {
  def create(start: LocalDate, end: LocalDate): Either[List[String], DateSpan] =
    Either
      .cond(start.isBefore(end), DateSpan(start, end), "Invalid DateSpan - start is not before the end")
      .leftMap(::(_, Nil))

  def unsafe(start: LocalDate, end: LocalDate)(using UnsafeTransformations): DateSpan =
    DateSpan(start, end)
}