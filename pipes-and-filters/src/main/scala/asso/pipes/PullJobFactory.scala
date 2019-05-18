package asso.pipes

import asso.pipes.pull.{EndNode, PullFlowBuilder, PullPipe, SourceNode}
import asso.pipes.pull.numbers.ProducerConsumerFactory

import scala.concurrent.ExecutionContext

object PullJobFactory {
  def buildConsoleJob(): () => Unit = {
    PullFlowBuilder.build(ProducerConsumerFactory.producerBuilderFromConsole())
      .withSimpleFilter(num => LongOperations.isMultipleFilter(num, 2))
      .buildJob(ProducerConsumerFactory.consumerToConsole)
  }

  def buildAlgorithm(outPath: String, primeFilter: String, subFilter: String, multiplesFiltered: String, multiplesFilter: String): () => Unit = {
    val primeSource = ProducerConsumerFactory.producerBuilderFromFile(primeFilter)
    val subSource = ProducerConsumerFactory.producerBuilderFromFile(subFilter)
    val filteredSource = ProducerConsumerFactory.producerBuilderFromFile(multiplesFiltered)
    val filerSource = ProducerConsumerFactory.producerBuilderFromFile(multiplesFilter)
    val outEndpoint = ProducerConsumerFactory.consumerToFile(outPath)

    buildAlgorithm(primeSource, subSource, filteredSource, filerSource, outEndpoint)
  }

  def buildSlowAlgorithm(outPath: String, primeFilter: String, subFilter: String, multiplesFiltered: String, multiplesFilter: String): () => Unit = {
    val byterate = 10

    val primeSource = ProducerConsumerFactory.producerBuilderFromFile(primeFilter)
    val subSource = ProducerConsumerFactory.slowProducerBuilderFromFile(subFilter, byterate)
    val filteredSource = ProducerConsumerFactory.producerBuilderFromFile(multiplesFiltered)
    val filerSource = ProducerConsumerFactory.slowProducerBuilderFromFile(multiplesFilter, byterate)
    val outEndpoint = ProducerConsumerFactory.consumerToFile(outPath)

    buildAlgorithm(primeSource, subSource, filteredSource, filerSource, outEndpoint)
  }

  private def buildAlgorithm(primeSource: ExecutionContext => SourceNode[Long], subSource: ExecutionContext => SourceNode[Long], filteredSource: ExecutionContext => SourceNode[Long], filerSource: ExecutionContext => SourceNode[Long], outEndpoint: PullPipe[Long] => EndNode[Long]) = {
    val primesFlow = PullFlowBuilder.build(primeSource)
      .withSimpleFilter(LongOperations.primeFilter)
    val subFlow = PullFlowBuilder.build(subSource)
    val multiplesFilterFilteredFlow = PullFlowBuilder.build(filteredSource)
    val multiplesFilterFlow = PullFlowBuilder.build(filerSource)

    val filterFlow = multiplesFilterFilteredFlow.withJoinFilter(multiplesFilterFlow, (a, b) => LongOperations.isMultipleFilter(a, b))

    val subbedFlow = subFlow.withJoinFilter(filterFlow, (a: Long, b: Long) => Value(a - b))
    primesFlow.withJoinFilter(subbedFlow, (a: Long, b: Long) => Value(a + b))
      .buildJob(outEndpoint)
  }
}
