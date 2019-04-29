package asso

import asso.pipes.pull.PullFlowBuilder
import asso.pipes.pull.numbers.ProducerConsumerFactory
import asso.pipes.{LongOperations, Value}

object PullJobFactory {
  def buildConsoleJob(): () => Unit = {
    PullFlowBuilder.build(ProducerConsumerFactory.producerFromConsole())
      .withSimpleFilter(num => LongOperations.isMultipleFilter(num, 2))
      .buildJob(ProducerConsumerFactory.consumerToConsole)
  }

  def buildAlgorithm(outPath: String, inPath1: String, inPath2: String): () => Unit = {
    val flow1 = PullFlowBuilder.build(ProducerConsumerFactory.producerFromFile(inPath1))
      .withSimpleFilter(LongOperations.primeFilter)

    PullFlowBuilder.build(ProducerConsumerFactory.producerFromFile(inPath2))
      .withJoinFilter(flow1, (num1: Long, num2: Long) => Value(num1 + num2))
      .buildJob(ProducerConsumerFactory.consumerToFile(outPath))
  }
}
