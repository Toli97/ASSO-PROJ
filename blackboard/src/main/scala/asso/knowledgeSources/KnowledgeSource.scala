package asso.knowledgeSources

import asso.Blackboard
import asso.message.Message

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext

trait Observer[T] {
  def receiveUpdate(message: Message[T]);
}

trait Subject[T] {
  private val observers: mutable.HashMap[Int, ListBuffer[Observer[T]]] = mutable.HashMap.empty;
  def addObserver(observer: Observer[T], topic: Int) = {
    if (observers.get(topic).isEmpty) {
      observers.put(topic, new ListBuffer[Observer[T]]());
    }
    observers.get(topic).get += observer
  };

  def notifyObservers(message: Message[T]) = {
    if (!observers.get(message.currentTopic).isEmpty) {
      observers.get(message.currentTopic).get.foreach((o) => {
        o.receiveUpdate(message)
      })
    }
  }
}


trait KnowledgeSource[T] extends Observer[T]{

  var receivedEof = false
  var blackboard: Blackboard[T] = _
  val messagesQueue1: mutable.Queue[Message[T]] = mutable.Queue[Message[T]]();
  var nextTopic: Int = KnowledgeSource.getCounter
  var endOfExecutionSubscribers: ListBuffer[KnowledgeSource[T]] = ListBuffer();

  implicit val ec: ExecutionContext = ExecutionContext.global

  def execute()

  def receiveUpdate(message: Message[T]): Unit = {
    messagesQueue1.enqueue(message)
  }

  def haveMessages() = messagesQueue1.nonEmpty

  def chain(knowledgeSource: KnowledgeSource[T]): KnowledgeSource[T] = {
    knowledgeSource.subscribeTopic(nextTopic)
    knowledgeSource.subscribeOnEndOfExecution(this)
    return knowledgeSource
  }

  def subscribeTopic(topic: Int): Unit = {
    blackboard.addObserver(this, topic)
  }

  def configure(blackboard: Blackboard[T]): Unit = {
    this.blackboard = blackboard
  }

  def subscribeOnEndOfExecution(ks: KnowledgeSource[T]): Unit = {
    endOfExecutionSubscribers += ks
  }

  def notifyEndOfExecutionSubscribers(): Unit = {
    endOfExecutionSubscribers.toList.foreach(ks => ks.receiveEndOfExecutionNotification() )
  }

  def receiveEndOfExecutionNotification(): Unit = {
    this.receivedEof = true
    notifyEndOfExecutionSubscribers()
  }

  def isEnabled: Boolean = !receivedEof

}

object KnowledgeSource {
  private var counter = 1
  def getCounter: Int = this.synchronized{
    counter += 1
    return counter - 1
  }
}
