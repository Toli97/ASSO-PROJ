package asso.pipes.pull

import asso.pipes.{Eof, NotNone, Value}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PullPipe[A](private val source: MessageProducer[A]) {

  def pull: Future[NotNone[A]] = for {
    msg <- source.produce if msg.isNotNone(msg)
  } yield msg match {
    case Value(value) => Value(value)
    case Eof() => Eof()
    case _ => throw new IllegalStateException("Shouldn't match NoValue")
  }

}
