package asso.knowledgeSources

import asso.message.{Eof, Value}

object FilterFactory {

  private def isPrime(n: Long): Boolean = {
    if (n < 2) return false
    val ceil: Long = Math.floor(Math.sqrt(n)).asInstanceOf[Long]
    var i: Long = 2L;
    while (i < ceil) {
      if (n % i == 0) return false
      i+=1
    }
    return true
  }

  private def isNotMultiple(n: Long): Boolean = {
    val multiplesList = List(2,3,5)
    return multiplesList.filter(l => n % l == 0).length == 0
  }

  def buildPrimesFilter(): ConditionFilter[Long] = {
    return new ConditionFilter[Long](isPrime)
  }

  def buildMultiplesFilter(): ConditionFilter[Long] = {
    return new ConditionFilter[Long](isNotMultiple)
  }

  def buildJoinMultiplesFilter(): JoinConditionFilter[Long] = {
    return new JoinConditionFilter[Long](isNotMultiple)
  }
}

// Filters messages using a condition function which should return true if the message should be forwarded, false otherwise.
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

// Filters messages from multiple topics. Finishes only when 2 EOFs are received.
case class JoinConditionFilter[T](val condition: (T) => Boolean) extends KnowledgeSource[T] {

  // needs to receive 2 EOF to terminate
  var receivedFirstEof = false;

  override def execute() {
    if (haveMessages()){
      val message = messagesQueue1.dequeue()
      message.setTopic(nextTopic)
      message match {
        case Value(value1, _) => {
          if (condition(value1)) {
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


}
