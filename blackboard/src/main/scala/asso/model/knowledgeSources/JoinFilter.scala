package asso.model.knowledgeSources

import asso.model.objects.{Eof, Message, Value}

import scala.collection.mutable

case class JoinFilter[T](operation: (T, T) => T) extends KnowledgeSource[T] {

  val messagesQueue2 = mutable.Queue[Message[T]]();

  override def receiveUpdate(message: Message[T]): Unit = {
    if (message.currentTopic == topic1) {
      messagesQueue1.enqueue(message)
    } else {
      messagesQueue2.enqueue(message)
    }
  }

  override def haveMessages(): Boolean = messagesQueue1.nonEmpty && messagesQueue2.nonEmpty

  override def setTopic(topic: Int): Unit = {
    super.setTopic(topic)
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
        case Value(value1) => {
          message2 match {
            case Value(value2) => {
              val newMessage = new Value[T](operation(value1, value2))
              newMessage.setTopic(message1.currentTopic)
              blackboard.addToQueue(newMessage)
            }
            case Eof() => {
              receivedEof = true
              blackboard.addToQueue(message2)
            }
          }
        }
        case Eof() => {
          receivedEof = true
          blackboard.addToQueue(message1)
        }
      }
    }
  }

}
