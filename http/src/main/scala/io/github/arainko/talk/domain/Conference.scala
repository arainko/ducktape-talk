package io.github.arainko.talk.domain

import java.time.LocalDate
import java.util.UUID
import io.github.arainko.newtypes.*

import io.github.arainko.talk.domain.DateSpan
import io.github.arainko.talk.domain.Talk
import io.github.arainko.talk.domain.UnsurprisingString

final case class Conference(id: Conference.Id, info: Conference.Info, talks: Vector[Talk])

object Conference {
  final case class Info(name: Conference.Name, dateSpan: DateSpan, city: Conference.City)

  object Id extends Newtype[UUID]
  export Id.Type as Id

  object Name extends NewtypeValidated[String, UnsurprisingString[20]]
  export Name.Type as Name

  object City extends NewtypeValidated[String, UnsurprisingString[20]]
  export City.Type as City
}
