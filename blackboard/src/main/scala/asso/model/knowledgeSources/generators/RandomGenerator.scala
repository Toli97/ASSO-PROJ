package asso.model.knowledgeSources.generators
import asso.model.Blackboard
import asso.model.objects.{Eof, Message, Value}

import scala.util.Random

case class RandomGenerator() extends Generator[Long]() {

  var counter = 10000;

  def isEnabled: Boolean = counter > 0
  val randomGenerator = new Random()

  override def execute() = {
    if(counter > 0) {
      val rnd = randomGenerator.nextLong()
      val newMessage = new Value(rnd)
      newMessage.setState(nextState)
      blackboard.addToQueue(newMessage)
      counter-=1
    } else if (counter == 0) {
      val newMessage = new Eof[Long]()
      newMessage.setState(nextState)
      blackboard.addToQueue(newMessage)
    }

  }

  override def receiveUpdate(message: Message[Long])= {}

  override def configure(blackboard: Blackboard[Long]): Unit = {
    this.blackboard = blackboard
  }
}
