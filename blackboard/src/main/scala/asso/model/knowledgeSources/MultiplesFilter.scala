package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.ToFilterMultiples

import scala.concurrent.Future

case class MultiplesFilter(blackboard: Blackboard) extends KnowledgeSource(blackboard = blackboard) {

  val multiplesList = List(2,3,5)
  val stage = ToFilterMultiples()

  override def isEnabled(): Boolean = !blackboard.isQueueEmpty(stage)

  override def execute(): Future[Unit] = Future {
    println("Executing Multiples Filter")
    keepGoing = true
    while (isEnabled && keepGoing) {
     val objectToFilter = blackboard.pollFromQueue(stage)
     if (multiplesList.filter(l => objectToFilter.value % l == 0).length == 0) {
       // advance object stage and put it back in the blackboard
       objectToFilter.advanceStage()
       blackboard.addToQueue(objectToFilter)
     }
    }
  }

}
