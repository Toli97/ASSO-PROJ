package asso.model.knowledgeSources

import asso.model.Blackboard

import scala.concurrent.{ExecutionContext, Future}

abstract class KnowledgeSource(blackboard: Blackboard) {

  implicit val ec: ExecutionContext = ExecutionContext.global
  var keepGoing = true

  def isEnabled(): Boolean

  def execute(): Future[Unit]

  def stop() = keepGoing = false

}
