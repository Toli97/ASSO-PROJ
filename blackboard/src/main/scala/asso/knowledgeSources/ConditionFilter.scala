package asso.knowledgeSources

import asso.message.{Eof, Value}

case class ConditionFilter[T](val condition: (T) => Boolean) extends KnowledgeSource[T] {

  override def execute() {
    if (haveMessages()){
      val message = messagesQueue1.dequeue()
      message.setTopic(nextTopic)
      message match {
        case Value(value1, _) => {
          if (condition(value1)) {
            // advance object stage and put it back in the blackboard
            blackboard.addToQueue(message)
          }
        }
        case Eof(_) => {
          blackboard.addToQueue(message);
          println("Condition Filter Finished")
          receivedEof = true;
        }
      }
    }

  }
}