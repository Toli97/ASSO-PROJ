package asso.pipes

sealed trait Message[A] {
  def isNotNone(): Boolean = this.isInstanceOf[NotNone[A]]
}

trait Optional[A] extends Message[A] // either has a value or no value

sealed trait NotNone[A] extends Message[A] // either a value or an EOF

final case class Value[A](value: A) extends Optional[A] with NotNone[A]

final case class NoValue[A]() extends Optional[A]

final case class Eof[A]() extends NotNone[A]
