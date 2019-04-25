package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.ProcessingStage

import scala.concurrent.{ExecutionContext, Future}

abstract class KnowledgeSource(blackboard: Blackboard, nextState: ProcessingStage) {

  implicit val ec: ExecutionContext = ExecutionContext.global

  def isEnabled(): Boolean

  def execute(): Future[Unit]

  def stop()

}
