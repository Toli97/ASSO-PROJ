package asso.pipes.pull

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class PullPipe[A](source: MessageProducer[A]) {

  private def isNotNone(msg: Message[A]): Boolean = msg match {
    case NoValue() => false
    case _ => true
  }

  def pull: Future[NotNone[A]] = for {
    msg <- source.produce if isNotNone(msg)
  } yield msg match {
    case Value(value) => Value(value)
    case EndOfInput() => EndOfInput()
  }

}
