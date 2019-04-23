package asso.pipes.pull

abstract class Message[A]

case class NotNone[A]() extends Message[A]

case class Value[A](value: A) extends NotNone[A]

case class NoValue[A]() extends Message[A]

case class EndOfInput[A]() extends NotNone[A]
