package asso.model

import asso.model.knowledgeSources.KnowledgeSource

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext

/**
  * Controls the knowledge sources that act on the blackboard
  * @param blackboard
  */
class Controller[I](blackboard: Blackboard[I]) {
  implicit val ec: ExecutionContext = ExecutionContext.global

  var knowledgeSources: ListBuffer[KnowledgeSource[I]] = ListBuffer.empty


  def addKnowledgeSources(list: List[KnowledgeSource[I]]) = {
    knowledgeSources = knowledgeSources ++ list
  }

  def execute() = {
    println("Executing controller")
    knowledgeSources.foreach((k) => {
      k.configure(blackboard)
    })
    while(knowledgeSources.nonEmpty) {
      knowledgeSources = knowledgeSources.filter(k => k.isEnabled)
      knowledgeSources.foreach(k => {
        k.execute();
      })
    }
  }

}
