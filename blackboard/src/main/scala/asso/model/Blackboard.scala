package asso.model

import asso.model.objects.IntWrapper

class Blackboard {
  var objects: List[IntWrapper] = List.empty

  def addObject(o: IntWrapper) = o :: objects
}