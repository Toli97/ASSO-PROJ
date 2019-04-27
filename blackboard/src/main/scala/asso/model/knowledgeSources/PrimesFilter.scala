package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.ToFilterPrime

import scala.concurrent.Future

case class PrimesFilter(blackboard: Blackboard) extends KnowledgeSource(blackboard = blackboard) {
  val stage = ToFilterPrime()

  override def isEnabled(): Boolean = !blackboard.isQueueEmpty(stage)

  override def execute(): Future[Unit] = Future {
    println("Executing Primes Filter")
    keepGoing = true
    while (isEnabled && keepGoing) {
      val objectToFilter = blackboard.pollFromQueue(stage)
      if (isPrime(objectToFilter.value)) {
        // advance object stage and put it back in the blackboard
        objectToFilter.advanceStage()
        blackboard.addToQueue(objectToFilter)
      }
    }
  }

  private def isPrime(number: Long): Boolean = {
    if (number < 2) return false
    val ceil: Long = Math.floor(Math.sqrt(number)).asInstanceOf[Long]
    for (i <- 2L to ceil) {
      if (number % i == 0) return false
    }
    return true

  }
}
