package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.ProcessingStage

import scala.concurrent.Future

case class JoinFilter(blackboard: Blackboard, src1: ProcessingStage, src2: ProcessingStage, operation: (Long, Long) => Long) extends KnowledgeSource(blackboard = blackboard){

  override def isEnabled(): Boolean = !blackboard.isQueueEmpty(src1) && !blackboard.isQueueEmpty(src2)

  override def execute(): Future[Unit] = Future {
    println("Executing Add Filter")
    keepGoing = true
    while (isEnabled && keepGoing) {
      val object1 = blackboard.pollFromQueue(src1)
      val object2 = blackboard.pollFromQueue(src2)
      object1.value = operation(object1.value, object2.value)
      object1.advanceStage()
      blackboard.addToQueue(object1)
    }
  }
}
