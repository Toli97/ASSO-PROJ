package asso.model

import asso.model.knowledgeSources.KnowledgeSource

import scala.concurrent.{ExecutionContext, Future}

class Controller(blackboard: Blackboard) {
  implicit val ec: ExecutionContext = ExecutionContext.global

  var knowledgeSources: List[KnowledgeSource] = List.empty

  def addKnowledgeSource(src: KnowledgeSource) = src :: knowledgeSources

  def execute() = Future {
    var futures: List[Future[Unit]] = List.empty
    knowledgeSources.foreach(k => {
      if (k.isEnabled()) {
        futures = k.execute() :: futures
      }
    })
    Future.sequence(futures)
  }

  def stop() = {
    knowledgeSources.foreach(k => k.stop())
  }
}
