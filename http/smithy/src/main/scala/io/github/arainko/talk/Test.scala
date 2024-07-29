// package io.github.arainko.talk

// import smithy4s.*
// import smithy4s.schema.SchemaVisitor
// import smithy4s.schema.Alt.Dispatcher
// import smithy4s.schema.Primitive
// import smithy4s.schema.Alt
// import smithy4s.schema.CollectionTag
// import smithy4s.schema.Field
// import smithy4s.schema.EnumValue
// import cats.Show
// import smithy4s.schema.Field.Wrapped
// import smithy4s.schema.Field.FolderK
// import smithy4s.capability.EncoderK
// import smithy4s.schema.Alt.Precompiler
// import io.github.arainko.test.Person
// import io.github.arainko.test.Age
// import cats.syntax.all.*

// class ShowVisitor extends SchemaVisitor[Show] { self =>
//   private given Show[ByteArray] = _.array.mkString("[", ",", "]")

//   private given Show[Document] = doc => doc.show

//   private given Show[Timestamp] = ts => ts.conciseDateTime

//   private val primitiveShow = Primitive.deriving[Show]

//   private def folder[S](struct: S) =
//     new FolderK[Schema, S, Show] {
//       def onOptional[A](label: String, instance: smithy4s.schema.Schema[A], get: S => Option[A]): cats.Show[Option[A]] =
//         a => get(struct).map(apply(instance).show).toString

//       def onRequired[A](label: String, instance: smithy4s.schema.Schema[A], get: S => A): cats.Show[A] =
//         a => apply(instance).show(get(struct))
//     }

//   private given EncoderK[Show, String] with {
//     override def apply[A](fa: Show[A], a: A): String = fa.show(a)
//     override def absorb[A](f: A => String): Show[A] = a => f(a)
//   }

//   private val precompiler: Precompiler[Schema, Show] = new {

//     override def apply[A](label: String, instance: Schema[A]): Show[A] =
//       a => s"$label: ${self.apply(instance)}"
//   }

//   override def primitive[P](shapeId: ShapeId, hints: Hints, tag: Primitive[P]): Show[P] = primitiveShow(tag)

//   override def struct[S](
//     shapeId: ShapeId,
//     hints: Hints,
//     fields: Vector[Field[Schema, S, ?]],
//     make: IndexedSeq[Any] => S
//   ): Show[S] =
//     struct => {
//       val shownFields =
//         fields.map { field =>
//           field.getDefault
//           val fieldInstance = field.foldK(folder(struct))
//           val shownField = fieldInstance.show(field.get(struct))
//           s"${field.label} -> $shownField"
//         }
//       shownFields.mkString(s"${shapeId.name}(", ", ", ")")
//     }

//   override def collection[C[a], A](
//     shapeId: ShapeId,
//     hints: Hints,
//     tag: CollectionTag[C],
//     member: Schema[A]
//   ): Show[C[A]] = coll => {
//     val show = apply(member)
//     tag.iterator(coll).map(show.show).mkString(s"${tag.name}(", ", ", ")")
//   }

//   override def enumeration[E](
//     shapeId: ShapeId,
//     hints: Hints,
//     values: List[EnumValue[E]],
//     total: E => EnumValue[E]
//   ): Show[E] = en => total(en).stringValue
  

//   override def union[U](
//     shapeId: ShapeId,
//     hints: Hints,
//     alternatives: Vector[Alt[Schema, U, ?]],
//     dispatch: Dispatcher[Schema, U]
//   ): Show[U] = dispatch.compile(precompiler)
  

//   override def map[K, V](shapeId: ShapeId, hints: Hints, key: Schema[K], value: Schema[V]): Show[Map[K, V]] = ???

//   override def lazily[A](suspend: Lazy[Schema[A]]): Show[A] = 
//     apply(suspend.value)

//   override def refine[A, B](schema: Schema[A], refinement: Refinement[A, B]): Show[B] = 
//     apply(schema).contramap(refinement.from)

//   override def biject[A, B](schema: Schema[A], bijection: Bijection[A, B]): Show[B] = 
//     apply(schema).contramap(bijection.from)

// }

// object Test extends App {
//   val cos = 1
//   val show = new ShowVisitor().apply(Person.schema)
//   val p = Person("Leks", List("1", "2", "3", "4"), Some(Age(1)))

//   println(show.show(p))
// }
