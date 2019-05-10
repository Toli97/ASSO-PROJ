package asso.model.knowledgeSources

import java.util.concurrent.LinkedBlockingQueue

import asso.model.Blackboard
import asso.model.objects.{Eof, Message, Value}

import scala.concurrent.Future

case class JoinFilter[I](operation: (I, I) => I) extends KnowledgeSource[I] {

  var receivedEof = false
  val messages = new LinkedBlockingQueue[Message[I]]();

  override def receiveUpdate(message: Message[I]): Unit = {
      messages.put(message)
  }

  override def execute() = Future {
    val message1 = messages.take();
    val message2 = messages.take();
    message1.setState(nextState)
    message2.setState(nextState)
    message1 match {
      case Value(value1) => {
        message2 match {
          case Value(value2) => {
            val newMessage = new Value[I](operation(value1, value2))
            newMessage.setState(message1.getCurrentState())
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

  override def configure(blackboard: Blackboard[I]): Unit = {
    this.blackboard = blackboard
    blackboard.addObserver(this, myState)
  }

  override def isEnabled: Boolean = !receivedEof
}
