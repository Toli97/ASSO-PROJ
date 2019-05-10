package asso.model

import asso.model.knowledgeSources.Subject
import asso.model.objects.Message

class Blackboard[I] extends Subject[I]{


  def addToQueue(element: Message[I]): Unit = {
    notifyObservers(element);
  }

}
