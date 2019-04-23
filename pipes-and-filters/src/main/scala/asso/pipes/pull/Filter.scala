package asso.pipes.pull

import asso.pipes.{Eof, Message, Optional, Value}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait MessageProducer[A] {
  def produce: Future[Message[A]]
}


class SimpleFilter[In, Out](val source: PullPipe[In], val operation: In => Optional[Out]) extends MessageProducer[Out] {

  override def produce: Future[Message[Out]] = source.pull map {
    case Value(value) => operation(value)
    case Eof() => Eof()
  }
}

class JoinFilter[In1, In2, Out](val source1: PullPipe[In1], val source2: PullPipe[In2], val operation: (In1, In2) => Optional[Out]) extends MessageProducer[Out] {

  override def produce: Future[Message[Out]] = for {
    opt1 <- source1.pull
    opt2 <- source2.pull
  } yield (opt1, opt2) match {
    case (Eof(), _) => Eof()
    case (_, Eof()) => Eof()
    case (Value(val1), Value(val2)) => operation(val1, val2)
  }
}