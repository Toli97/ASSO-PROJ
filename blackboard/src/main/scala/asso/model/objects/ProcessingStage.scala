package asso.model.objects

trait ProcessingStage

case class ToFilterMultiples() extends ProcessingStage
case class ToFilterPrime() extends ProcessingStage
case class ToSub1() extends ProcessingStage
case class ToSub2() extends ProcessingStage
case class ToAdd1() extends ProcessingStage
case class ToAdd2() extends ProcessingStage
case class ToOutput() extends ProcessingStage
case class Finished() extends ProcessingStage

