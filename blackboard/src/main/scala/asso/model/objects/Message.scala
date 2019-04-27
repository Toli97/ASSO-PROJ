package asso.model.objects


abstract class Message[I](val stageChain: Vector[ProcessingStage]) {
  var currentStageIdx: Int = 0

  def getCurrentStage(): ProcessingStage = {
    return stageChain(currentStageIdx)
  }

  def getNextStage(): ProcessingStage = {
    if (currentStageIdx + 1 == stageChain.length) {
      return stageChain(currentStageIdx)
    }
    return stageChain(currentStageIdx+1)
  }
  def advanceStage(): Unit = {
    if (currentStageIdx + 1 < stageChain.length) {
      currentStageIdx += 1
    }
  }
}

case class Value[I](var value: I, chain: Vector[ProcessingStage]) extends Message[I](chain)

case class Eof[I](chain: Vector[ProcessingStage]) extends Message[I](chain)


