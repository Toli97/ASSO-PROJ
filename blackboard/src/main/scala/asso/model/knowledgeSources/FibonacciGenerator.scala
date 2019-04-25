package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.{LongWrapper, ProcessingStage}

import scala.concurrent.Future

/**
  * Generates fibonacci numbers and puts them on the blackboard
 *
  * @param blackboard Where to put the objects produced
  * @param nextStage Stage of the objects that are put on the blackboard
  */
case class FibonacciGenerator(blackboard: Blackboard, nextStage: ProcessingStage) extends KnowledgeSource(blackboard = blackboard, nextStage = nextStage) {
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
      blackboard.addObject(new LongWrapper(nextLong, nextStage))
      lastButOne = last
      last = nextLong
      counter += 1
    }

  }

  override def stop(): Unit = {
    keepGoing = false
  }
}
