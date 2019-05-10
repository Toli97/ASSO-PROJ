package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.Message

import scala.collection.mutable
import scala.concurrent.ExecutionContext

trait Observer[I] {
  def receiveUpdate(message: Message[I]);
}

trait Subject[I] {
  private val observers: mutable.HashMap[Int, Observer[I]] = mutable.HashMap.empty;
  def addObserver(observer: Observer[I], state: Int) = {
    observers.put(state, observer)
  };

  def notifyObservers(message: Message[I]) = {
    observers.get(message.getCurrentState()).foreach((o) => {
      o.receiveUpdate(message)
    })
  }
}


trait KnowledgeSource[I] extends Observer[I]{

  var blackboard: Blackboard[I] = _
  var myState: Int = KnowledgeSource.getCounter
  var nextState: Int = _

  implicit val ec: ExecutionContext = ExecutionContext.global

  def execute()

  def configure(blackboard: Blackboard[I])

  def chain(knowledgeSource: KnowledgeSource[I]) = {
    nextState = knowledgeSource.myState;
  }

  def isEnabled: Boolean

}

object KnowledgeSource {
  private var counter = 0
  def getCounter: Int = this.synchronized{
    counter += 1
    return counter - 1
  }
}
