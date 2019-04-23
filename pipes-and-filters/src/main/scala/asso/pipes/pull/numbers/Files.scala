package asso.pipes.pull.numbers

import java.io.{File, PrintWriter}
import java.util.Scanner

import asso.pipes.pull.MessageProducer
import asso.pipes.{Eof, NotNone, Value}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.control.Breaks._

class FileLongProducer(filename: String) extends MessageProducer[Long] {
  private val scanner = new Scanner(new File(filename))

  override def produce: Future[NotNone[Long]] = Future {
    if (scanner.hasNext()) {
      Value(scanner.next())
    } else {
      Eof()
    }
  }
}

class FileLongConsumer(filename: String, messageProducer: MessageProducer[Long]) {
  private val writter = new PrintWriter(filename)

  private def getValue = Await.result(messageProducer.produce, 5.second)


  def consumeAll = {
    // TODO make loop scala like
    breakable {
      while (true) {
        getValue match {
          case Value(value) => writter.print(value)
          case Eof() => break
        }
      }
    }
  }
}