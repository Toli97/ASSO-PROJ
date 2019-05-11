package asso.pipes

import asso.pipes.push.{EndNode, PushFlowBuilder, PushPipe, SourceNode}
import asso.pipes.push.numbers.ProducerConsumerFactory

import scala.concurrent.ExecutionContext

object PushJobFactory {
  def buildConsoleJob(): () => Unit = {
    PushFlowBuilder.build(ProducerConsumerFactory.producerBuilderFromConsole())
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

  private def buildAlgorithm(primeSource: ExecutionContext => SourceNode[Long], subSource: ExecutionContext => SourceNode[Long], filteredSource: ExecutionContext => SourceNode[Long], filerSource: ExecutionContext => SourceNode[Long], outEndpoint: PushPipe[Long] => EndNode[Long]) = {
    val primesFlow = PushFlowBuilder.build(primeSource)
      .withSimpleFilter(LongOperations.primeFilter)
    val subFlow = PushFlowBuilder.build(subSource)
    val multiplesFilterFilteredFlow = PushFlowBuilder.build(filteredSource)
    val multiplesFilterFlow = PushFlowBuilder.build(filerSource)

    val filterFlow = multiplesFilterFilteredFlow.withJoinFilter(multiplesFilterFlow, (a, b) => LongOperations.isMultipleFilter(a, b))

    val subbedFlow = subFlow.withJoinFilter(filterFlow, (a: Long, b: Long) => Value(a - b))
    primesFlow.withJoinFilter(subbedFlow, (a: Long, b: Long) => Value(a + b))
      .buildJob(outEndpoint)
  }
}
