package asso.model.knowledgeSources

import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.Date

import asso.model.objects.{Eof, Message, Value}

case class Output[T]() extends KnowledgeSource[T]() {
  val fileName: String = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date())
  val printStream = new PrintStream(fileName)

  // If i'm receiving updates to fast it might not process all the messages
  // T need a queue to put the resolved messages
  override def receiveUpdate(message: Message[T]): Unit = {
    println("Output received message")
    messagesQueue1.enqueue(message);
  }

  override def execute() {
    if (haveMessages()){
      val message: Message[T] = messagesQueue1.dequeue();
      message.setTopic(nextTopic)
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
  }
}
