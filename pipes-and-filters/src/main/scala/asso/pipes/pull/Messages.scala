package asso.pipes.pull

object messages {

  object extensions {
    implicit class EitherMessage[A](either: Either[Value[A], NoValue[A]]) {
      def toMessage: Message[A] = either match {
        case Left(Value(v)) => Value(v)
        case Right(NoValue()) => NoValue()
      }
    }
  }
}

sealed abstract class Message[A] {
  def isNotNone(msg: Message[A]): Boolean = msg.isInstanceOf[NotNone[A]]
}

sealed abstract class NotNone[A]() extends Message[A]

case class Value[A](value: A) extends NotNone[A]

case class NoValue[A]() extends Message[A]

case class Eof[A]() extends NotNone[A]
