package asso.pipes.pull

import asso.pipes.{Eof, NoValue, NotNone, Value}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future, blocking}

class PullPipe[A](source: MessageProducer[A], duration: Duration) {

  def pull(): Future[NotNone[A]] = blocking {
    Await.result(source.produce, duration) match {
      case NoValue() => pull()
      case Value(v) => Future.successful(Value(v))
      case Eof() => Future.successful(Eof())
    }
  }
}
