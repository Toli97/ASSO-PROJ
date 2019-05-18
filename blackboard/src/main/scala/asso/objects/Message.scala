package asso.objects


abstract class Message[T]() {

  private var topic: Int = _

  def currentTopic: Int = {
    return topic
  }

  def setTopic(nextTopic: Int) = topic = nextTopic
}

case class Value[T](var value: T, private var topic: Int) extends Message[T]

case class Eof[T](private var topic: Int) extends Message[T]()


