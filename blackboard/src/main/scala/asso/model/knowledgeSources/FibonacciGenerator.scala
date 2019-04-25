package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.{IntWrapper, ProcessingStage}

import scala.concurrent.Future

case class FibonacciGenerator(blackboard: Blackboard, nextState: ProcessingStage) extends KnowledgeSource(blackboard = blackboard, nextState = nextState) {
  val numbersToGenerate = 10000
  var counter = 0
  var keepGoing = true


  override def isEnabled: Boolean = counter < numbersToGenerate

  var last = 0
  var lastButOne = 0

  override def execute() = Future {
    keepGoing = true
    while (isEnabled && keepGoing) {
      var nextInt: Int = 0
      if (counter > 1) nextInt = last + lastButOne
      else if (counter == 1) nextInt = 1
      blackboard.addObject(new IntWrapper(nextInt, nextState))
      lastButOne = last
      last = nextInt
      counter += 1
    }

  }

  override def stop(): Unit = {
    keepGoing = false
  }
}
