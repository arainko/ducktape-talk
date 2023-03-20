package io.github.arainko.talk

import java.time.LocalDate
import java.util.UUID
import io.github.arainko.newtypes.*
import eu.timepit.refined.string
import eu.timepit.refined.collection.*
import eu.timepit.refined.string.Trimmed
import eu.timepit.refined.boolean.And
import eu.timepit.refined.types.string.NonEmptyString

final case class Conference(
  id: Conference.Id,
  name: Conference.Name,
  dateSpan: DateSpan,
  city: Conference.City,
  talks: Vector[Talk]
)

type UnsurprisingString[Size <: Int] = Trimmed And NonEmpty And MaxSize[Size]

object Conference {
  object Id extends Newtype[UUID]
  export Id.Type as Id

  object Name extends NewtypeValidated[String, UnsurprisingString[20]]
  export Name.Type as Name

  object City extends NewtypeValidated[String, UnsurprisingString[20]]
  export City.Type as City
}

//todo add validation
final case class DateSpan(start: LocalDate, end: LocalDate)

final case class Talk(id: Talk.Id, name: Talk.Name, elevatorPitch: Talk.ElevatorPitch, presenter: Person)

object Talk {
  object Id extends Newtype[UUID]
  export Id.Type as Id

  object Name extends NewtypeValidated[String, UnsurprisingString[20]]
  export Name.Type as Name

  object ElevatorPitch extends NewtypeValidated[String, UnsurprisingString[300]]
  export ElevatorPitch.Type as ElevatorPitch
}

final case class Person(name: Person.Name, bio: Person.Bio, pronouns: Option[Pronouns])

object Person {
  object Name extends NewtypeValidated[String, UnsurprisingString[100]]
  export Name.Type as Name

  object Bio extends NewtypeValidated[String, UnsurprisingString[300]]
  export Bio.Type as Bio
}

//todo: should there be a `Custom` option? prolly yes...
enum Pronouns {
  case `they/them`, `she/her`, `he/him`
}

