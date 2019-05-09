package asso.pipes.pull.numbers

import java.io.{FileInputStream, FileOutputStream, InputStream, PrintStream}
import java.util.Scanner

import asso.pipes.pull.{EndNode, PullPipe, SourceNode}
import asso.pipes.{AssoValues, Eof, NotNone, Value}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future, blocking}
import scala.util.control.Breaks._

object ProducerConsumerFactory {
  def producerBuilderFromFile(filepath: String): ExecutionContext => SourceNode[Long] = ec => new LongProducer(new FileInputStream(filepath), ec)

  def producerBuilderFromConsole(): ExecutionContext => SourceNode[Long] = ec => new LongProducer(System.in, ec)

  def consumerToFile(filepath: String)(messageProducer: PullPipe[Long]):  EndNode[Long] = new LongConsumer(new PrintStream(new FileOutputStream(filepath, false)), messageProducer, AssoValues.DefaultDuration)

  def consumerToConsole(messageProducer: PullPipe[Long]): EndNode[Long] = new LongConsumer(System.out, messageProducer, AssoValues.DefaultDuration)
}

class LongProducer(private val is: InputStream, implicit private val ec: ExecutionContext) extends SourceNode[Long] {
  private val scanner = new Scanner(is)

  override def produce: Future[NotNone[Long]] = Future {
    blocking { // TODO is this correct
      if (scanner.hasNext()) {
        Value(scanner.next().toLong)
      } else {
        Eof()
      }
    }
  }
}


class LongConsumer(private val printer: PrintStream, private val pipe: PullPipe[Long], duration: Duration) extends EndNode[Long] {

  private def getValue = Await.result(pipe.pull, duration)

  override def consumeAll() : Unit = {
    // TODO make loop scala like
    breakable {
      while (true) {
        getValue match {
          case Value(value) => printer.println(value)
          case Eof() => break
        }
      }
    }
  }
}
