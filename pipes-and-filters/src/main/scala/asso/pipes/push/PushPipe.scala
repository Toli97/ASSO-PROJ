package asso.pipes.push

import asso.pipes.{Eof, NoValue, NotNone, Value}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future, blocking}

class PushPipe[A](source: MessageProducer[A], duration: Duration) {

  def push(): Future[NotNone[A]] = blocking {
    Await.result(source.produce, duration) match {
      case NoValue() => push()
      case Value(v) => Future.successful(Value(v))
      case Eof() => Future.successful(Eof())
    }
  }
}
