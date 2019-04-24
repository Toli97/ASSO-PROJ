package asso.pipes.pull

import asso.pipes.{Eof, NoValue, NotNone, Value}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future, blocking}

class PullPipe[A](source: MessageProducer[A]) {
  private val Duration = 30.second

  def pull(): Future[NotNone[A]] = blocking {
    Await.result(source.produce, Duration) match {
      case NoValue() => pull()
      case Value(v) => Future.successful(Value(v))
      case Eof() => Future.successful(Eof())
    }
  }
}
