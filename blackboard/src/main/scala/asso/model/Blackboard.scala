package asso.model

import asso.model.knowledgeSources.Subject
import asso.model.objects.Message

class Blackboard[T] extends Subject[T]{


  def addToQueue(element: Message[T]): Unit = {
    notifyObservers(element);
  }

}
