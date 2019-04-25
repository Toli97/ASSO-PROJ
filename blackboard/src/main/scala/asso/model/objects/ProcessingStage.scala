package asso.model.objects

/**
  * Processing Stage of each object.
  * KnowledgeSources use the ProcessingStage to determine if they should act on each object
  */
trait ProcessingStage

case class ToFilterMultiples() extends ProcessingStage
case class ToFilterPrime() extends ProcessingStage
case class ToSub1() extends ProcessingStage
case class ToSub2() extends ProcessingStage
case class ToAdd1() extends ProcessingStage
case class ToAdd2() extends ProcessingStage
case class ToOutput() extends ProcessingStage
case class Finished() extends ProcessingStage

