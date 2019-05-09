package asso

import asso.pipes.pull.PullFlowBuilder
import asso.pipes.pull.numbers.ProducerConsumerFactory
import asso.pipes.{LongOperations, Value}

object PullJobFactory {
  def buildConsoleJob(): () => Unit = {
    PullFlowBuilder.build(ProducerConsumerFactory.producerBuilderFromConsole())
      .withSimpleFilter(num => LongOperations.isMultipleFilter(num, 2))
      .buildJob(ProducerConsumerFactory.consumerToConsole)
  }

  def buildAlgorithm(outPath: String, primeFilter: String, subFilter: String, multiplesFiltered: String, multiplesFilter: String): () => Unit = {
    val primesFlow = PullFlowBuilder.build(ProducerConsumerFactory.producerBuilderFromFile(primeFilter))
      .withSimpleFilter(LongOperations.primeFilter)

    val subFlow = PullFlowBuilder.build(ProducerConsumerFactory.producerBuilderFromFile(subFilter))

    val multiplesFilterFilteredFlow = PullFlowBuilder.build(ProducerConsumerFactory.producerBuilderFromFile(multiplesFiltered))
    val multiplesFilterFlow = PullFlowBuilder.build(ProducerConsumerFactory.producerBuilderFromFile(multiplesFilter))
    val filterFlow = multiplesFilterFilteredFlow.withJoinFilter(multiplesFilterFlow, (a, b) => LongOperations.isMultipleFilter(a, b))

    val subbedFlow = subFlow.withJoinFilter(filterFlow, (a: Long, b: Long) => Value(a - b))

    primesFlow.withJoinFilter(subbedFlow, (a: Long, b: Long) => Value(a + b))
        .buildJob(ProducerConsumerFactory.consumerToFile(outPath))
  }
}
