package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.{LongWrapper, ProcessingStage}

import scala.concurrent.Future

case class FibonacciGenerator(blackboard: Blackboard, nextState: ProcessingStage) extends KnowledgeSource(blackboard = blackboard, nextState = nextState) {
  val numbersToGenerate = 70
  var counter = 0
  var keepGoing = true
  var last: Long = 0
  var lastButOne: Long = 0

  override def isEnabled: Boolean = counter < numbersToGenerate

  override def execute() = Future {
    println("Executing Fibonacci Generator")
    println("isEnabled: " + isEnabled)
    keepGoing = true
    while (isEnabled && keepGoing) {
      var nextLong: Long = 0
      if (counter > 1) nextLong = last + lastButOne
      else if (counter == 1) nextLong = 1
      blackboard.addObject(new LongWrapper(nextLong, nextState))
      lastButOne = last
      last = nextLong
      counter += 1
    }

  }

  override def stop(): Unit = {
    keepGoing = false
  }
}
