package asso.model

import java.util.concurrent.{ConcurrentHashMap, ConcurrentLinkedQueue}

import asso.model.objects.{Message, ProcessingStage}

class Blackboard[I] {

  var board: ConcurrentHashMap[ProcessingStage, ConcurrentLinkedQueue[Message[I]]] = new ConcurrentHashMap[ProcessingStage, ConcurrentLinkedQueue[Message[I]]]()

  def addToQueue(element: Message[I]): Unit = {
    board.putIfAbsent(element.getCurrentStage(), new ConcurrentLinkedQueue[Message[I]]())
    board.get(element.getCurrentStage()).add(element)
  }


  def pollFromQueue(processingStage: ProcessingStage): Message[I] = {
    board.putIfAbsent(processingStage, new ConcurrentLinkedQueue[Message[I]]())
    return board.get(processingStage).poll()
  }

  def isQueueEmpty(processingStage: ProcessingStage): Boolean = {
    board.putIfAbsent(processingStage, new ConcurrentLinkedQueue[Message[I]]())
    return board.get(processingStage).isEmpty()
  }

  def getQueue(processingStage: ProcessingStage): ConcurrentLinkedQueue[Message[I]] = {
    board.putIfAbsent(processingStage, new ConcurrentLinkedQueue[Message[I]]())
    return board.get(processingStage)
  }
}