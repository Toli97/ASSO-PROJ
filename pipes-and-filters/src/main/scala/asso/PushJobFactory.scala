package asso

import asso.pipes.push.PushFlowBuilder
import asso.pipes.push.numbers.ProducerConsumerFactory
import asso.pipes.{LongOperations, Value}

object PushJobFactory {
  def buildConsoleJob(): () => Unit = {
    PushFlowBuilder.build(ProducerConsumerFactory.producerFromConsole())
      .withSimpleFilter(num => LongOperations.isMultipleFilter(num, 2))
      .buildJob(ProducerConsumerFactory.consumerToConsole)
  }

  def buildAlgorithm(outPath: String, inPath1: String, inPath2: String): () => Unit = {
    val flow1 = PushFlowBuilder.build(ProducerConsumerFactory.producerFromFile(inPath1))
      .withSimpleFilter(LongOperations.primeFilter)

    PushFlowBuilder.build(ProducerConsumerFactory.producerFromFile(inPath2))
      .withJoinFilter(flow1, (num1: Long, num2: Long) => Value(num1 + num2))
      .buildJob(ProducerConsumerFactory.consumerToFile(outPath))
  }
}
