package asso.pipes.pull

import asso.pipes.{Eof, Message, Optional, Value}

import scala.concurrent.{ExecutionContext, Future}

trait MessageProducer[A] {
  def produce: Future[Message[A]]
}

trait SourceNode[A] extends MessageProducer[A]

trait EndNode[A] {
  def consumeAll(): Unit
}

class SimpleFilter[In, Out](private val source: PullPipe[In], private val operation: In => Optional[Out], implicit private val ec: ExecutionContext) extends MessageProducer[Out] {

  override def produce: Future[Message[Out]] =  for {opt <- source.pull}
    yield opt match {
      case Value(value) => operation(value)
      case Eof() => Eof()
    }
}

class JoinFilter[In1, In2, Out](private val source1: PullPipe[In1], private val source2: PullPipe[In2], private val operation: (In1, In2) => Optional[Out], implicit private val ec: ExecutionContext) extends MessageProducer[Out] {

  override def produce: Future[Message[Out]] = {
    // separate for parallelism
    val fut1 = source1.pull
    val fut2 = source2.pull

    for {
      opt1 <- fut1
      opt2 <- fut2
    } yield (opt1, opt2) match {
      case (Eof(), _) => Eof()
      case (_, Eof()) => Eof()
      case (Value(val1), Value(val2)) => operation(val1, val2)
    }
  }
}