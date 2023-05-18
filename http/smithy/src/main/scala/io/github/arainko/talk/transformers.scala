package io.github.arainko.talk

import io.github.arainko.ducktape.Transformer
import io.github.arainko.talk.domain.model.Pronouns
import smithy4s.{ Bijection, Surjection }

import java.time.LocalDate
import io.github.arainko.talk.API.Pronouns.sheher
import io.github.arainko.talk.API.Pronouns.theythem
import io.github.arainko.talk.API.Pronouns.hehim

given bijectTo[A, B](using bijection: Bijection[A, B]): Transformer[A, B] = bijection.to

given bijectFrom[A, B](using bijection: Bijection[A, B]): Transformer[B, A] = bijection.from

given surjectTo[A, B](using surjection: Surjection[A, B]): Transformer.Fallible[Either[List[String], _], A, B] =
  surjection.to(_).left.map(_ :: Nil)

given surjectFrom[A, B](using surjection: Surjection[A, B]): Transformer[B, A] = surjection.from

given toApiPronouns: Transformer[Pronouns, API.Pronouns] = {
  case Pronouns.`They/them` => API.Pronouns.theythem
  case Pronouns.`She/her`   => API.Pronouns.sheher
  case Pronouns.`He/him`    => API.Pronouns.hehim
}

given fromApiPronouns: Transformer[API.Pronouns, Pronouns] = {
  case API.Pronouns.sheher => Pronouns.`She/her`
  case API.Pronouns.theythem => Pronouns.`They/them`
  case API.Pronouns.hehim => Pronouns.`He/him`
}

