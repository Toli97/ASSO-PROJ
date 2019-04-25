package asso.model

import asso.model.objects.LongWrapper

import scala.collection.mutable.ListBuffer

class Blackboard {
  var objects: ListBuffer[LongWrapper] = ListBuffer.empty

  def addObject(o: LongWrapper) = {
    objects += o
  }
}