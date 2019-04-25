package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.{LongWrapper, ProcessingStage}

import scala.concurrent.Future
import scala.util.Random

case class RandomGenerator(blackboard: Blackboard, nextState: ProcessingStage) extends KnowledgeSource(blackboard = blackboard, nextStage = nextState) {

  var counter = 10000;
  var keepGoing = true

  override def isEnabled: Boolean = counter > 0
  val randomGenerator = new Random()

  override def execute() = Future {
    keepGoing = true
    while(isEnabled && keepGoing) {
      val rnd = randomGenerator.nextInt()
      blackboard.addObject(new LongWrapper(rnd, nextState))
      counter-=1
    }
  }

  override def stop(): Unit = keepGoing = false
}
