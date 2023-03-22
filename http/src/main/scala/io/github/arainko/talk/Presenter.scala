package io.github.arainko.talk

import java.time.LocalDate
import java.util.UUID
import io.github.arainko.newtypes.*
import eu.timepit.refined.string
import eu.timepit.refined.collection.*
import eu.timepit.refined.string.Trimmed
import eu.timepit.refined.boolean.And
import eu.timepit.refined.types.string.NonEmptyString

final case class Presenter(name: Presenter.Name, bio: Presenter.Bio, pronouns: Option[Pronouns])

object Presenter {
  object Name extends NewtypeValidated[String, UnsurprisingString[100]]
  export Name.Type as Name

  object Bio extends NewtypeValidated[String, UnsurprisingString[300]]
  export Bio.Type as Bio
}