package asso.model.knowledgeSources.generators

import asso.model.Blackboard
import asso.model.objects.{Eof, Message, Value}

case class FibonacciGenerator() extends Generator[Long] {
  val numbersToGenerate = 70
  var counter = 0
  var last: Long = 0
  var lastButOne: Long = 0

  def isEnabled: Boolean = counter <= numbersToGenerate

  override def execute() = {
    println("Fibonacci counter: " + counter)
    if (counter < numbersToGenerate) {
      var nextLong: Long = 0
      if (counter > 1) nextLong = last + lastButOne
      else if (counter == 1) nextLong = 1
      val newMessage = new Value(nextLong)
      newMessage.setState(nextState)
      blackboard.addToQueue(newMessage)
      lastButOne = last
      last = nextLong
    }
    else if (counter >= numbersToGenerate) {
      val newMessage = new Eof[Long]()
      newMessage.setState(nextState)
      blackboard.addToQueue(newMessage)
      println("Fibonacci Generator Finished")
    }
    counter += 1

  }

  override def receiveUpdate(message: Message[Long]) {}

  override def configure(blackboard: Blackboard[Long]): Unit = {
    this.blackboard = blackboard
  }
}