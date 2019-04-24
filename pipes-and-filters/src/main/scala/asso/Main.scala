package asso

import asso.pipes.LongOperations
import asso.pipes.pull.PullFlowBuilder
import asso.pipes.pull.numbers.ProducerConsumerFactory

object Main {
  def main(args: Array[String]): Unit = {

    runConsolePull()
  }

  def runConsolePull() = {
    PullFlowBuilder.build(ProducerConsumerFactory.producerFromConsole())
      .withSimpleFilter(num => LongOperations.multiplesFilter(num, 2))
      .buildJob(ProducerConsumerFactory.consumerToConsole())()
  }
}

