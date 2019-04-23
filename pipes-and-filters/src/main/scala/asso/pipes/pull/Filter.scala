package asso.pipes.pull

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import asso.pipes.pull.messages.extensions._

trait MessageProducer[A] {
  def produce: Future[Message[A]]
}

trait Filter[A] {

}

abstract class SimpleFilter[A](val source: PullPipe[A]) extends MessageProducer[A] with Filter[A] {

  override def produce: Future[Message[A]] = source.pull map {
    case Value(value) => operate(value).toMessage
    case Eof() => Eof()
  }

  def operate(source: A): Either[Value[A], NoValue[A]]
}

abstract class JoinFilter[A](val source1: PullPipe[A], val source2: PullPipe[A]) extends MessageProducer[A] with Filter[A] {

  override def produce: Future[Message[A]] = for {
    opt1 <- source1.pull
    opt2 <- source2.pull
  } yield (opt1, opt2) match {
    case (Eof(), _) => Eof()
    case (_, Eof()) => Eof()
    case (Value(val1), Value(val2)) => operate(val1, val2).toMessage
  }

  def operate(source1: A, source2: A): Either[Value[A], NoValue[A]]
}