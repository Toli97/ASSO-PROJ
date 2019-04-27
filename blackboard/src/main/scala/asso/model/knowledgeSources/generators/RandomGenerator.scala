package asso.model.knowledgeSources.generators

import asso.model.Blackboard
import asso.model.objects.{LongWrapper, ProcessingStage}

import scala.concurrent.Future
import scala.util.Random

case class RandomGenerator(blackboard: Blackboard, chain: Vector[ProcessingStage]) extends Generator(blackboard, chain) {

  var counter = 10000;

  override def isEnabled: Boolean = counter > 0
  val randomGenerator = new Random()

  override def execute() = Future {
    keepGoing = true
    while(isEnabled && keepGoing) {
      val rnd = randomGenerator.nextInt()
      blackboard.addToQueue(new LongWrapper(rnd, chain))
      counter-=1
    }
  }
}
