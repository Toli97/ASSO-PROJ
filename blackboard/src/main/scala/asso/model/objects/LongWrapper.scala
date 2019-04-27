package asso.model.objects

class LongWrapper(var value: Long, val stageChain: Vector[ProcessingStage]) {
  private var currentStageIdx: Int = 0

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


