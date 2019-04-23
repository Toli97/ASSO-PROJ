package asso.pipes.pull

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class PullPipe[A](source: MessageProducer[A]) {

  def pull: Future[NotNone[A]] = for {
    msg <- source.produce if msg.isNotNone(msg)
  } yield msg match {
    case Value(value) => Value(value)
    case Eof() => Eof()
    case _ => throw new IllegalStateException("Shouldn't match NoValue")
  }

}
