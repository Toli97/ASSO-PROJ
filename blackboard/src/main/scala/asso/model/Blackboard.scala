package asso.model

import java.util.concurrent.{ConcurrentHashMap, ConcurrentLinkedQueue}

import asso.model.objects.{LongWrapper, ProcessingStage}

class Blackboard {

  var board: ConcurrentHashMap[ProcessingStage, ConcurrentLinkedQueue[LongWrapper]] = new ConcurrentHashMap[ProcessingStage, ConcurrentLinkedQueue[LongWrapper]]()

  def addToQueue(element: LongWrapper): Unit = {
    board.putIfAbsent(element.getCurrentStage(), new ConcurrentLinkedQueue[LongWrapper]())
    board.get(element.getCurrentStage()).add(element)
  }


  def pollFromQueue(processingStage: ProcessingStage): LongWrapper = {
    board.putIfAbsent(processingStage, new ConcurrentLinkedQueue[LongWrapper]())
    return board.get(processingStage).poll()
  }

  def isQueueEmpty(processingStage: ProcessingStage): Boolean = {
    board.putIfAbsent(processingStage, new ConcurrentLinkedQueue[LongWrapper]())
    return board.get(processingStage).isEmpty()
  }

  def getQueue(processingStage: ProcessingStage): ConcurrentLinkedQueue[LongWrapper] = {
    board.putIfAbsent(processingStage, new ConcurrentLinkedQueue[LongWrapper]())
    return board.get(processingStage)
  }
}