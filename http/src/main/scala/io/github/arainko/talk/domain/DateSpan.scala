package io.github.arainko.talk.domain

import java.time.LocalDate
import cats.syntax.either.*

final case class DateSpan private (start: LocalDate, end: LocalDate)

object DateSpan {
  def create(start: LocalDate, end: LocalDate): Either[::[String], DateSpan] =
    Either
      .cond(start.isBefore(end), DateSpan(start, end), "Invalid DateSpan - start is not before the end")
      .leftMap(::(_, Nil))
}