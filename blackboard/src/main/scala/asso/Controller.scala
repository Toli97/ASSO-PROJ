package asso

import asso.knowledgeSources.KnowledgeSource

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
    list.foreach(ks => ks.configure(blackboard))
  }

  def execute() = {
    println("Executing controller")

    while(knowledgeSources.nonEmpty) {
      knowledgeSources = knowledgeSources.filter(k => k.isEnabled)
      knowledgeSources.foreach(k => {
        k.execute();
      })
    }
  }

}
