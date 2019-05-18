package asso

import asso.knowledgeSources.Subject
import asso.objects.Message

class Blackboard[T] extends Subject[T]{


  def addToQueue(element: Message[T]): Unit = {
    notifyObservers(element);
  }

}
