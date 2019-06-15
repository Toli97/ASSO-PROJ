package asso

import asso.knowledgeSources.Subject
import asso.message.Message

class Blackboard[T] extends Subject[T]{


  def addToQueue(element: Message[T]): Unit = {
    notifyObservers(element);
  }

}
