package asso.knowledgeSources

import asso.objects.{Eof, Value}

case class JoinConditionFilter[T](val condition: (T) => Boolean) extends KnowledgeSource[T] {

  // needs to receive 2 EOF to terminate
  var receivedFirstEof = false;

  override def execute() {
    if (haveMessages()){
      val message = messagesQueue1.dequeue()
      println("CondFilter processing message")
      message.setTopic(nextTopic)
      message match {
        case Value(value1, _) => {
          if (condition(value1)) {
            // advance object stage and put it back in the blackboard
            blackboard.addToQueue(message)
          }
        }
        case Eof(_) => {
          if (receivedFirstEof) {
            receivedEof = true
            blackboard.addToQueue(message)
            println("Condition Filter Finished")
          }
          receivedFirstEof = true
        }
      }
    }
  }

  override def setTopic(topic: Int): Unit = {
    blackboard.addObserver(this, topic)
  }


}