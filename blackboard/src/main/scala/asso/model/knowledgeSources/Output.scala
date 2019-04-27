package asso.model.knowledgeSources

import java.io.PrintStream

import asso.model.Blackboard
import asso.model.objects.ToOutput
import java.text.SimpleDateFormat
import java.util.Date

import scala.concurrent.Future

case class Output(blackboard: Blackboard) extends KnowledgeSource(blackboard = blackboard){
  val stage = ToOutput()
  val fileName: String = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date())
  val printStream = new PrintStream(fileName)

  override def isEnabled(): Boolean = !blackboard.isQueueEmpty(stage)

  override def execute(): Future[Unit] = Future {
    println("Executing Output Filter")
    keepGoing = true
    while (isEnabled && keepGoing) {
      val objectToWrite = blackboard.pollFromQueue(stage)
      printStream.println(objectToWrite.value)
      objectToWrite.advanceStage()
      blackboard.addToQueue(objectToWrite)
    }
  }

  override def stop(): Unit = {
    super.stop()
    printStream.close()
  }
}
