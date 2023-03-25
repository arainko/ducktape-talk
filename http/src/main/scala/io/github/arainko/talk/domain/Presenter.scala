package io.github.arainko.talk.domain

import io.github.arainko.newtypes.*

import io.github.arainko.talk.domain.Pronouns
import io.github.arainko.talk.domain.UnsurprisingString

final case class Presenter(name: Presenter.Name, bio: Presenter.Bio, pronouns: Option[Pronouns])

object Presenter {
  object Name extends NewtypeValidated[String, UnsurprisingString[100]]
  export Name.Type as Name

  object Bio extends NewtypeValidated[String, UnsurprisingString[300]]
  export Bio.Type as Bio
}
