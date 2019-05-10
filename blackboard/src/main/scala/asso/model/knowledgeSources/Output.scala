package asso.model.knowledgeSources

import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.LinkedBlockingQueue

import asso.model.Blackboard
import asso.model.objects.{Eof, Message, Value}

case class Output[I]() extends KnowledgeSource[I]() {
  val fileName: String = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date())
  val printStream = new PrintStream(fileName)

  val nextMessages = new LinkedBlockingQueue[Message[I]]();
  var receivedEof = false

  // If i'm receiving updates to fast it might not process all the messages
  // I need a queue to put the resolved messages
  override def receiveUpdate(message: Message[I]): Unit = {
    println("Output received message")
    nextMessages.put(message);
  }

  override def execute() =  {
    println("Output waiting for message in queue")
    val message: Message[I] = nextMessages.take();
    message.setState(nextState)
    blackboard.addToQueue(message)
    message match {
      case Value(value1) => {
        printStream.println(value1.toString())
      }
      case Eof() => {
        println("Output Finished")
        receivedEof = true
      }
    }
  }

  override def configure(blackboard: Blackboard[I]): Unit = {
    this.blackboard = blackboard
    blackboard.addObserver(this, myState)
  }

  override def isEnabled: Boolean = !receivedEof
}
