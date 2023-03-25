package io.github.arainko.talk.domain

import java.util.UUID
import io.github.arainko.newtypes.*
import io.github.arainko.talk.domain.UnsurprisingString
import io.github.arainko.talk.domain.Presenter

final case class Talk(id: Talk.Id, name: Talk.Name, elevatorPitch: Talk.ElevatorPitch, presenter: Presenter)

object Talk {
  object Id extends Newtype[UUID]
  export Id.Type as Id

  object Name extends NewtypeValidated[String, UnsurprisingString[20]]
  export Name.Type as Name

  object ElevatorPitch extends NewtypeValidated[String, UnsurprisingString[300]]
  export ElevatorPitch.Type as ElevatorPitch
}
