package io.github.arainko.talk.infrastructure.model

import io.github.arainko.talk.domain.model
import io.github.arainko.ducktape.*
import io.github.arainko.newtypes.UnsafeTransformations

final case class Presenter(name: String, bio: String, pronouns: Option[String])

object Presenter {
  given fromDomain: Transformer[model.Presenter, Presenter] = presenter =>
    presenter.into[Presenter].transform(Field.computed(_.pronouns, _.pronouns.map(_.toString)))

  given toDomain(using UnsafeTransformations): Transformer[Presenter, model.Presenter] = presenter =>
    presenter.into[model.Presenter].transform(Field.computed(_.pronouns, _.pronouns.flatMap(model.Pronouns.fromString)))
}
