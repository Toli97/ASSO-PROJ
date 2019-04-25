package asso.model

import asso.model.knowledgeSources.KnowledgeSource

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class Controller(blackboard: Blackboard) {
  implicit val ec: ExecutionContext = ExecutionContext.global

  var knowledgeSources: List[KnowledgeSource] = List.empty

  def addKnowledgeSource(src: KnowledgeSource) = {
    knowledgeSources = src :: knowledgeSources
  }

  def execute(): Future[Unit] = Future {
    println("Executing controller")
    println("Knowledge sources length: " + knowledgeSources.length)
    var futures: List[Future[Unit]] = List.empty
    knowledgeSources.foreach(k => {
      if (k.isEnabled()) {
        futures = k.execute() :: futures
      }
    })
    Await.result(Future.sequence(futures), Duration.Inf)
  }

  def stop() = {
    knowledgeSources.foreach(k => k.stop())
  }
}
