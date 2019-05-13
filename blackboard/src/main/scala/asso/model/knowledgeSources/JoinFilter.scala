package asso.model.knowledgeSources

import asso.model.objects.{Eof, Message, Value}

import scala.collection.mutable

case class JoinFilter[T](operation: (T, T) => T) extends KnowledgeSource[T] {

  var receivedEof = false
  val messagesQueue1 = new mutable.Queue[Message[T]]();
  val messagesQueue2 = new mutable.Queue[Message[T]]();

  override def execute() {
    if (!messagesQueue1.isEmpty && !messagesQueue2.isEmpty) {
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

  override def isEnabled: Boolean = !receivedEof
}
