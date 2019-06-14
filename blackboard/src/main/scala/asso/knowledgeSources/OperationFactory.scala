package asso.knowledgeSources
import asso.message.{Eof, Message, Value}

import scala.collection.mutable
object OperationFactory {

  def buildAdd() = new OperationFilter[Long]((a,b) => a + b)
  def buildSub() = new OperationFilter[Long]((a,b) => a - b)
}


case class OperationFilter[T](operation: (T, T) => T) extends KnowledgeSource[T] {

  val messagesQueue2 = mutable.Queue[Message[T]]();
  var topic1: Int = 0

  override def receiveUpdate(message: Message[T]): Unit = {
    if (message.currentTopic == topic1) {
      messagesQueue1.enqueue(message)
    } else {
      messagesQueue2.enqueue(message)
    }
  }

  override def haveMessages(): Boolean = messagesQueue1.nonEmpty && messagesQueue2.nonEmpty

  override def subscribeTopic(topic: Int): Unit = {
    blackboard.addObserver(this, topic)
    if (topic1 == 0) {
      topic1 = topic
    }
  }


  override def execute() {
    if (haveMessages()) {
      val message1 = messagesQueue1.dequeue();
      val message2 = messagesQueue2.dequeue();
      message1.setTopic(nextTopic)
      message2.setTopic(nextTopic)
      message1 match {
        case Value(value1, _) => {
          message2 match {
            case Value(value2, _) => {
              val newMessage = new Value[T](operation(value1, value2), nextTopic)
              blackboard.addToQueue(newMessage)
            }
            case Eof(_) => {
              println("Operation Filter finished")
              receivedEof = true
              blackboard.addToQueue(message2)
              notifyEndOfExecutionSubscribers()
            }
          }
        }
        case Eof(_) => {
          println("Operation Filter finished")
          receivedEof = true
          blackboard.addToQueue(message1)
          notifyEndOfExecutionSubscribers()
        }
      }
    }
  }

}
