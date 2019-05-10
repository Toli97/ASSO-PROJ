package asso.model.knowledgeSources

import java.util.concurrent.LinkedBlockingQueue

import asso.model.Blackboard
import asso.model.objects.{Eof, Message, Value}

case class ConditionFilter[I](val condition: (I) => Boolean) extends KnowledgeSource[I] {

  val nextMessages = new LinkedBlockingQueue[Message[I]]();
  var receivedEof = false;

  // If i'm receiving updates to fast it might not process all the messages
  // I need a queue to put the resolved messages
  override def receiveUpdate(message: Message[I]): Unit = {
    println("Condition Filter received message")
    nextMessages.put(message);
  }

  override def execute() = {
    println("CondFilter waiting for message in queue")
    val message = nextMessages.take();
    println("CondFilter processing message")
    message.setState(nextState)
    message match {
      case Value(value1) => {
        println("CondFilter processing value " + value1)
        if (condition(value1)) {
          // advance object stage and put it back in the blackboard
          blackboard.addToQueue(message)
        }
      }
      case Eof() => {
        blackboard.addToQueue(message);
        println("Condition Filter Finished")
        receivedEof = true;
      }
    }
  }

  override def configure(blackboard: Blackboard[I]): Unit = {
    this.blackboard = blackboard
    blackboard.addObserver(this, myState)
  }

  override def isEnabled: Boolean = !receivedEof
}