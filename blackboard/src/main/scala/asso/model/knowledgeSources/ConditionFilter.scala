package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.{Eof, ProcessingStage, Value}

import scala.concurrent.Future

case class ConditionFilter[I](blackboard: Blackboard[I], val stage: ProcessingStage, val condition: (I) => Boolean) extends KnowledgeSource(blackboard = blackboard)  {
  override def isEnabled(): Boolean = !blackboard.isQueueEmpty(stage)

  override def execute(): Future[Unit] = Future {
    println("Executing Condition Filter")
    keepGoing = true
    while (isEnabled && keepGoing) {
      val objectToFilter = blackboard.pollFromQueue(stage)
      objectToFilter match {
        case Value(value1, _) => {
          if (condition(value1)) {
            // advance object stage and put it back in the blackboard
            objectToFilter.advanceStage()
            blackboard.addToQueue(objectToFilter)
          }
        }
        case Eof(_) => {
          objectToFilter.advanceStage()
          blackboard.addToQueue(objectToFilter)
          stop()
        }
      }

    }
  }
}
