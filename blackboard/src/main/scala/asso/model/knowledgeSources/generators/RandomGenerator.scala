package asso.model.knowledgeSources.generators
import asso.model.objects.{Eof, Message, Value}

import scala.util.Random

case class RandomGenerator() extends Generator[Long]() {

  var counter = 10000;

  def isEnabled: Boolean = counter > 0
  val randomGenerator = new Random()

  override def execute() {
    if(counter > 0) {
      val rnd = randomGenerator.nextLong()
      val newMessage = new Value(rnd)
      newMessage.setTopic(nextTopic)
      blackboard.addToQueue(newMessage)
      counter-=1
    } else if (counter == 0) {
      val newMessage = new Eof[Long]()
      newMessage.setTopic(nextTopic)
      blackboard.addToQueue(newMessage)
    }

  }

  override def receiveUpdate(message: Message[Long])= {}
}
