package asso.model.objects


abstract class Message[T]() {
  private var topic: Int = 0

  def currentTopic: Int = {
    return topic
  }

  def setTopic(nextTopic: Int) = topic = nextTopic
}

case class Value[T](var value: T) extends Message[T]

case class Eof[T]() extends Message[T]()


