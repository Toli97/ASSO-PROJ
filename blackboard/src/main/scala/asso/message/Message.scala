package asso.message


abstract class Message[T](var topic: Int) {


  def currentTopic: Int = {
    return topic
  }

  def setTopic(nextTopic: Int) = topic = nextTopic
}

case class Value[T](var value: T, private var newTopic: Int) extends Message[T](topic = newTopic)

case class Eof[T](private var newTopic: Int) extends Message[T](topic = newTopic)


