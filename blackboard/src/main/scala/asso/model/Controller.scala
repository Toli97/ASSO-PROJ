package asso.model

import asso.model.knowledgeSources.KnowledgeSource

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

/**
  * Controls the knowledge sources that act on the blackboard
  * @param blackboard
  */
class Controller[I](blackboard: Blackboard[I]) {
  implicit val ec: ExecutionContext = ExecutionContext.global
  val timer = new java.util.Timer()
  val task = new java.util.TimerTask {
    def run() = execute()
  }
  timer.schedule(task, 1000L, 1000L)

  var knowledgeSources: ListBuffer[KnowledgeSource[I]] = ListBuffer.empty

  def addKnowledgeSource(src: KnowledgeSource[I]) = {
    knowledgeSources += src
  }


  def execute(): Future[Unit] = Future {
    println("Executing controller")
    var futures: List[Future[Unit]] = List.empty
    knowledgeSources.foreach(k => {
      if (k.isEnabled()) futures = k.execute() :: futures
    })
    Await.result(Future.sequence(futures), Duration.Inf)
  }

  def stop() = {
    task.cancel()
    knowledgeSources.foreach(k => k.stop())
  }
}
