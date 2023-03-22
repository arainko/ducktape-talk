package io.github.arainko.talk

import java.time.LocalDate
import java.util.UUID
import io.github.arainko.newtypes.*

final case class Conference(
  id: Conference.Id,
  name: Conference.Name,
  dateSpan: DateSpan,
  city: Conference.City,
  talks: Vector[Talk]
)

object Conference {
  object Id extends Newtype[UUID]
  export Id.Type as Id

  object Name extends NewtypeValidated[String, UnsurprisingString[20]]
  export Name.Type as Name

  object City extends NewtypeValidated[String, UnsurprisingString[20]]
  export City.Type as City
}
