package io.github.arainko.talk

import io.github.arainko.ducktape.*
import io.github.arainko.newtypes.*
import eu.timepit.refined.numeric.*
import eu.timepit.refined.boolean.And
import io.github.arainko
import javax.xml.crypto.dsig.Transform

type CoolInt = CoolInt.Type
object CoolInt extends Newtype[Int]

final case class Prim(int: Int, int2: Int)

object CoolValidatedInt extends NewtypeValidated[Int, GreaterEqual[0]]

object Prim {
  type CoolValidatedInt = CoolValidatedInt.Type

}

final case class New(int: CoolInt, int2: CoolInt)
final case class NewValidated(int: Prim.CoolValidatedInt, int2: Prim.CoolValidatedInt)

object Main extends App {
  // println(CoolValidatedInt.make(-1))

  // val t = Transformer.Debug.showCode(summon[Transformer.Accumulating[Either[::[String], _], Prim, NewValidated]])

  // println(t.transform(Prim(1, -1)))

  // private given UnsafeTransformations = UnsafeTransformations.allow

  import io.gitbug.arainko.talk.generated.definitions.*

  // summon[Transformer.Accumulating[Either[::[String], _], GetTalk, Talk]]

  // Transformer.Debug.showCode {
  //   Transformer
  //     .define[GetConference, Conference]
  //     .accumulating[Either[::[String], _]]
  //     .build()
  // }

  Transformer.Debug.showCode {
    Transformer.define[Conference, GetConference].build()
  }

  // Transformer.Debug.showCode(summon[Transformer.Accumulating[Either[::[String], _], GetConference, Conference]])

  // Transformer.Debug.showCode(Prim(0, 0).to[New])

  // Transformer.forProducts[Prim, New]

  /*
  {
    val DefinitionBuilder_this: DefinitionBuilder[Conference, GetConference] =
      Transformer.define[Conference, GetConference]

    ((
      (from: Conference) =>
        ({
          val city$1: String = unwrappingTransformer.transform(from.city)
          val name$1: String = unwrappingTransformer.transform(from.name)
          val talks$1: Vector[GetTalk] = from.talks
            .map[GetTalk](
              (
                (src: Talk) =>
                  {
                    val x$2$proxy1: Product {
                      type MirroredMonoType >: GetTalk <: GetTalk
                      type MirroredType >: GetTalk <: GetTalk
                      type MirroredLabel >: "GetTalk" <: "GetTalk"
                      type MirroredElemTypes >: *:[UUID, *:[String, *:[String, *:[Presenter, EmptyTuple]]]] <: *:[
                        UUID,
                        *:[String, *:[String, *:[Presenter, EmptyTuple]]]
                      ]
                      type MirroredElemLabels >: *:[
                        "id",
                        *:["name", *:["elevatorPitch", *:["presenter", EmptyTuple]]]
                      ] <: *:["id", *:["name", *:["elevatorPitch", *:["presenter", EmptyTuple]]]]
                    } = GetTalk.$asInstanceOf$[
                      Product {
                        type MirroredMonoType >: GetTalk <: GetTalk
                        type MirroredType >: GetTalk <: GetTalk
                        type MirroredLabel >: "GetTalk" <: "GetTalk"
                        type MirroredElemTypes >: *:[UUID, *:[String, *:[String, *:[Presenter, EmptyTuple]]]] <: *:[
                          UUID,
                          *:[String, *:[String, *:[Presenter, EmptyTuple]]]
                        ]
                        type MirroredElemLabels >: *:[
                          "id",
                          *:["name", *:["elevatorPitch", *:["presenter", EmptyTuple]]]
                        ] <: *:["id", *:["name", *:["elevatorPitch", *:["presenter", EmptyTuple]]]]
                      }
                    ]

                    (inline$make$i1[Talk, GetTalk](ForProduct)(
                      ((
                        (source: Talk) =>
                          new GetTalk(
                            id            = uwnrappingTransformer.transform(source.id),
                            name          = unwrappingTransformer.transform(source.name),
                            elevatorPitch = unwrappingTransformer.transform(source.elevatorPitch),
                            presenter = {
                              val x$2$proxy2: Product {
                                type MirroredMonoType >: Presenter <: Presenter
                                type MirroredType >: Presenter <: Presenter
                                type MirroredLabel >: "Presenter" <: "Presenter"
                                type MirroredElemTypes >: *:[
                                  String,
                                  *:[String, *:[Option[Pronouns], EmptyTuple]]
                                ] <: *:[String, *:[String, *:[Option[Pronouns], EmptyTuple]]]
                                type MirroredElemLabels >: *:["name", *:["bio", *:["pronouns", EmptyTuple]]] <: *:[
                                  "name",
                                  *:["bio", *:["pronouns", EmptyTuple]]
                                ]
                              } = Presenter.$asInstanceOf$[
                                Product {
                                  type MirroredMonoType >: Presenter <: Presenter
                                  type MirroredType >: Presenter <: Presenter
                                  type MirroredLabel >: "Presenter" <: "Presenter"
                                  type MirroredElemTypes >: *:[
                                    String,
                                    *:[String, *:[Option[Pronouns], EmptyTuple]]
                                  ] <: *:[String, *:[String, *:[Option[Pronouns], EmptyTuple]]]
                                  type MirroredElemLabels >: *:["name", *:["bio", *:["pronouns", EmptyTuple]]] <: *:[
                                    "name",
                                    *:["bio", *:["pronouns", EmptyTuple]]
                                  ]
                                }
                              ]

                              (inline$make$i1[Presenter, Presenter](ForProduct)(
                                ((
                                  (`source₂`: Presenter) =>
                                    new Presenter(
                                      name = unwrappingTransformer.transform(`source₂`.name),
                                      bio  = unwrappingTransformer.transform(`source₂`.bio),
                                      pronouns = `source₂`.pronouns.map[Pronouns](
                                        (
                                          (`src₂`: Pronouns) =>
                                            {
                                              val x$1$proxy3: Sum {
                                                type MirroredMonoType >: Pronouns <: Pronouns
                                                type MirroredType >: Pronouns <: Pronouns
                                                type MirroredLabel >: "Pronouns" <: "Pronouns"
                                                type MirroredElemTypes >: *:[
                                                  They / them,
                                                  *:[She / her, *:[He / him, EmptyTuple]]
                                                ] <: *:[They / them, *:[She / her, *:[He / him, EmptyTuple]]]
                                                type MirroredElemLabels >: *:[
                                                  "They/them",
                                                  *:["She/her", *:["He/him", EmptyTuple]]
                                                ] <: *:["They/them", *:["She/her", *:["He/him", EmptyTuple]]]
                                              } = Pronouns.$asInstanceOf$[
                                                Sum {
                                                  type MirroredMonoType >: Pronouns <: Pronouns
                                                  type MirroredType >: Pronouns <: Pronouns
                                                  type MirroredLabel >: "Pronouns" <: "Pronouns"
                                                  type MirroredElemTypes >: *:[
                                                    They / them,
                                                    *:[She / her, *:[He / him, EmptyTuple]]
                                                  ] <: *:[They / them, *:[She / her, *:[He / him, EmptyTuple]]]
                                                  type MirroredElemLabels >: *:[
                                                    "They/them",
                                                    *:["She/her", *:["He/him", EmptyTuple]]
                                                  ] <: *:["They/them", *:["She/her", *:["He/him", EmptyTuple]]]
                                                }
                                              ]
                                              val x$2$proxy3: Sum {
                                                type MirroredMonoType >: Pronouns <: Pronouns
                                                type MirroredType >: Pronouns <: Pronouns
                                                type MirroredLabel >: "Pronouns" <: "Pronouns"
                                                type MirroredElemTypes >: *:[
                                                  They / them,
                                                  *:[She / her, *:[He / him, EmptyTuple]]
                                                ] <: *:[They / them, *:[She / her, *:[He / him, EmptyTuple]]]
                                                type MirroredElemLabels >: *:[
                                                  "They/them",
                                                  *:["She/her", *:["He/him", EmptyTuple]]
                                                ] <: *:["They/them", *:["She/her", *:["He/him", EmptyTuple]]]
                                              } = Pronouns.$asInstanceOf$[
                                                Sum {
                                                  type MirroredMonoType >: Pronouns <: Pronouns
                                                  type MirroredType >: Pronouns <: Pronouns
                                                  type MirroredLabel >: "Pronouns" <: "Pronouns"
                                                  type MirroredElemTypes >: *:[
                                                    They / them,
                                                    *:[She / her, *:[He / him, EmptyTuple]]
                                                  ] <: *:[They / them, *:[She / her, *:[He / him, EmptyTuple]]]
                                                  type MirroredElemLabels >: *:[
                                                    "They/them",
                                                    *:["She/her", *:["He/him", EmptyTuple]]
                                                  ] <: *:["They/them", *:["She/her", *:["He/him", EmptyTuple]]]
                                                }
                                              ]

                                              (inline$make$i2[Pronouns, Pronouns](ForCoproduct)(
                                                ((
                                                  (`source₃`: Pronouns) =>
                                                    if (`source₃`.isInstanceOf[They / them.type]) They / them
                                                    else if (`source₃`.isInstanceOf[She / her.type]) She / her
                                                    else if (`source₃`.isInstanceOf[He / him.type]) He / him
                                                    else
                                                      throw new RuntimeException(
                                                        "Unhandled condition encountered during Coproduct Transformer derivation"
                                                      )
                                                ): Transformer[Pronouns, Pronouns])
                                              ): ForCoproduct[Pronouns, Pronouns])
                                            }.transform(`src₂`)
                                        )
                                      )
                                    )
                                ): Transformer[Presenter, Presenter])
                              ): ForProduct[Presenter, Presenter])
                            }.transform(source.presenter)
                          )
                      ): Transformer[Talk, GetTalk])
                    ): ForProduct[Talk, GetTalk])
                  }.transform(src)
              )
            )
            .to[Vector[GetTalk] & Iterable[GetTalk]](iterableFactory[GetTalk])
          val id$1: UUID = uwnrappingTransformer.transform(from.id)
          val dateSpan$1: DateSpan = {
            val x$2$proxy5: Product {
              type MirroredMonoType >: DateSpan <: DateSpan
              type MirroredType >: DateSpan <: DateSpan
              type MirroredLabel >: "DateSpan" <: "DateSpan"
              type MirroredElemTypes >: *:[LocalDate, *:[LocalDate, EmptyTuple]] <: *:[
                LocalDate,
                *:[LocalDate, EmptyTuple]
              ]
              type MirroredElemLabels >: *:["start", *:["end", EmptyTuple]] <: *:["start", *:["end", EmptyTuple]]
            } = DateSpan.$asInstanceOf$[
              Product {
                type MirroredMonoType >: DateSpan <: DateSpan
                type MirroredType >: DateSpan <: DateSpan
                type MirroredLabel >: "DateSpan" <: "DateSpan"
                type MirroredElemTypes >: *:[LocalDate, *:[LocalDate, EmptyTuple]] <: *:[
                  LocalDate,
                  *:[LocalDate, EmptyTuple]
                ]
                type MirroredElemLabels >: *:["start", *:["end", EmptyTuple]] <: *:["start", *:["end", EmptyTuple]]
              }
            ]

            (inline$make$i1[DateSpan, DateSpan](ForProduct)(
              (((`source₄`: DateSpan) => new DateSpan(start = `source₄`.start, end = `source₄`.end)): Transformer[
                DateSpan,
                DateSpan
              ])
            ): ForProduct[DateSpan, DateSpan])
          }.transform(from.dateSpan)
          new GetConference(id = id$1, name = name$1, dateSpan = dateSpan$1, city = city$1, talks = talks$1)
        }: GetConference)
    ): Transformer[Conference, GetConference])
  }
  */

}
