package asso.pipes.pull

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait MessageProducer[A] {
  def produce: Future[Message[A]]
}

trait Filter[A] {
  def unpack(either: Either[Value[A], NoValue[A]]): Message[A] = either match {
    case Left(Value(v)) => Value(v)
    case Right(NoValue()) => NoValue()
  }
}

abstract class SimpleFilter[A](val source: PullPipe[A]) extends MessageProducer[A] with Filter[A] {

  override def produce: Future[Message[A]] = source.pull map {
    case Value(value) => unpack(operate(value))
    case EndOfInput() => EndOfInput()
  }

  def operate(source: A): Either[Value[A], NoValue[A]]
}

abstract class JoinFilter[A](val source1: PullPipe[A], val source2: PullPipe[A]) extends MessageProducer[A] with Filter[A] {

  override def produce: Future[Message[A]] = for {
    opt1 <- source1.pull
    opt2 <- source2.pull
  } yield (opt1, opt2) match {
    case (EndOfInput(), _) => EndOfInput()
    case (_, EndOfInput()) => EndOfInput()
    case (Value(val1), Value(val2)) => unpack(operate(val1, val2))
  }

  def operate(source1: A, source2: A): Either[Value[A], NoValue[A]]
}