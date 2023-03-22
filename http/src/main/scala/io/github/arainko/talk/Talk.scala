package io.github.arainko.talk

import java.time.LocalDate
import java.util.UUID
import io.github.arainko.newtypes.*
import eu.timepit.refined.string
import eu.timepit.refined.collection.*
import eu.timepit.refined.string.Trimmed
import eu.timepit.refined.boolean.And
import eu.timepit.refined.types.string.NonEmptyString

final case class Talk(id: Talk.Id, name: Talk.Name, elevatorPitch: Talk.ElevatorPitch, presenter: Presenter)

object Talk {
  object Id extends Newtype[UUID]
  export Id.Type as Id

  object Name extends NewtypeValidated[String, UnsurprisingString[20]]
  export Name.Type as Name

  object ElevatorPitch extends NewtypeValidated[String, UnsurprisingString[300]]
  export ElevatorPitch.Type as ElevatorPitch
}