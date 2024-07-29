package preprocessors

import software.amazon.smithy.build._
import software.amazon.smithy.model._
import software.amazon.smithy.model.shapes.ShapeId
import software.amazon.smithy.model.shapes.ShapeType
import software.amazon.smithy.model.shapes.MemberShape
import _root_.software.amazon.smithy.model.shapes.StructureShape
// import software.amazon.smithy.model.shapes._
// import software.amazon.smithy.model.traits._


class SchemaTransformationTest extends ProjectionTransformer {

  def getName() = {
    "SchemaTransformationTest"
  }

  def transform(ctx: TransformContext) : Model = {
    println("============ DOING WORK ======")
    // Apply

    val model: Model = ctx.getModel()
    val extensions = ShapeId.from("io.github.arainko.test#extensions")
    // val toRemove = ctx
      // .getModel()
      // .getShapesWithTrait(ShapeId.from("preprocessed#removeBeforeCodegen"))

    // ctx.getTransformer().removeShapes(ctx.getModel(), toRemove)
    // Create a transformer
    // val transformer = ctx.getTransformer()

    // Iterate over all shapes and find integer fields to change to long
    val updatedShapes = model.shapes()
      .filter(_.getType == ShapeType.STRUCTURE)
      .map[StructureShape] { shape =>
        val structure = shape.asStructureShape().get()
        val updatedMembers = structure.getAllMembers().values().stream().map[MemberShape] { member =>
          member.getAllTraits().values().stream()
          if (model.expectShape(member.getTarget).getType == ShapeType.INTEGER) {
            member.findTrait(extensions).map[Unit] { hint =>
              println(hint.toNode().expectObjectNode().getMembers().get(node.Node.from("format")))
              println(hint.toNode.expectObjectNode.expectMember("key").expectStringNode())
              println(hint.toNode.expectObjectNode.expectMember("value").expectStringNode())
            }
            member.toBuilder.target(ShapeId.from("smithy.api#Long")).build()
          } else {
            member
          }
        }.toList()

        structure.toBuilder.members(updatedMembers).build()
      }.toList()

    model.toBuilder.addShapes(updatedShapes).build()



    // ctx
    //   .getTransformer()
    //   .mapShapes(model, shape =>
    //     if (shape.getType() == ShapeType.STRUCTURE) {
    //       ???
    //     } else shape 
      
    //   )
      // .changeShapeType(model, java.util.Map.of(ShapeId.from("smithy.api#Integer"), ShapeType.LONG))
  }
}
