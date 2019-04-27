package asso.model.knowledgeSources.generators

import asso.model.Blackboard
import asso.model.objects.{LongWrapper, ProcessingStage}

import scala.concurrent.Future

/**
  * Generates fibonacci numbers and puts them on the blackboard
 *
  * @param blackboard Where to put the objects produced
  * @param chain All the stages that the objects produced should go through
  */
case class FibonacciGenerator(blackboard: Blackboard, chain: Vector[ProcessingStage]) extends Generator(blackboard, chain) {
  val numbersToGenerate = 70
  var counter = 0
  var last: Long = 0
  var lastButOne: Long = 0

  override def isEnabled: Boolean = counter < numbersToGenerate

  override def execute() = Future {
    println("Executing Fibonacci Generator")
    keepGoing = true
    while (isEnabled && keepGoing) {
      var nextLong: Long = 0
      if (counter > 1) nextLong = last + lastButOne
      else if (counter == 1) nextLong = 1
      blackboard.addToQueue(new LongWrapper(nextLong, chain))
      lastButOne = last
      last = nextLong
      counter += 1
    }

  }

}
