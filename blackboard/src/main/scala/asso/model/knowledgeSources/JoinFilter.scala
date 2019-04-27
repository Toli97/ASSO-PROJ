package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.{Eof, ProcessingStage, Value}

import scala.concurrent.Future

case class JoinFilter[I](blackboard: Blackboard[I], src1: ProcessingStage, src2: ProcessingStage, operation: (I, I) => I) extends KnowledgeSource(blackboard = blackboard){

  override def isEnabled(): Boolean = !blackboard.isQueueEmpty(src1) && !blackboard.isQueueEmpty(src2)

  override def execute(): Future[Unit] = Future {
    println("Executing Add Filter")
    keepGoing = true
    while (isEnabled && keepGoing) {
      val object1 = blackboard.pollFromQueue(src1)
      val object2 = blackboard.pollFromQueue(src2)
      object1 match {
        case Value(value1, chain1) => {
          object2 match {
            case Value(value2, _) => {
              val newObject = new Value[I](operation(value1, value2), chain1)
              newObject.currentStageIdx = object1.currentStageIdx
              newObject.advanceStage()
              blackboard.addToQueue(newObject)
            }
            case Eof(_) => {
              object2.advanceStage()
              blackboard.addToQueue(object2)
              stop()
            }
          }
        }
        case Eof(_) => {
          object1.advanceStage()
          blackboard.addToQueue(object1)
          stop()
        }
      }

    }
  }
}
