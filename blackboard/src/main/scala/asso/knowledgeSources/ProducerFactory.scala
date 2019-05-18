package asso.knowledgeSources

import java.io.{FileInputStream, InputStream}
import java.util.Scanner

import asso.benchmarking.SlowInputStream
import asso.objects.{Eof, Value}

object ProducerFactory {
  def fromFile(filePath: String) = new LongProducer(new FileInputStream((filePath)))

  def slowFromFile(filePath: String, bytesPerSec: Int): LongProducer = {
    val period = (1000 * 1.0 / bytesPerSec).toInt
    val fs = new FileInputStream(filePath)
    val slowIs = new SlowInputStream(fs, period)
    return new LongProducer(slowIs)
  }
}

case class LongProducer(is: InputStream) extends KnowledgeSource[Long] {

  private val scanner = new Scanner(is)

  override def execute(): Unit = {
    if (scanner.hasNext()) {
      val msg = new Value[Long](scanner.next().toLong, nextTopic)
      blackboard.addToQueue(msg)
    } else {
      val msg = new Eof[Long](nextTopic)
      blackboard.addToQueue(msg)
      receivedEof = true
    }
  }
}
