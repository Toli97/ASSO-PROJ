package asso.pipes.pull

import asso.pipes.{AssoValues, Optional}
import scala.concurrent.ExecutionContext


class PullFlowBuilder[In] private (private val source: MessageProducer[In], private val ec: ExecutionContext) {

  private def sourcePipe = new PullPipe(source, AssoValues.DefaultDuration)

  def withSimpleFilter[Out](operation: In => Optional[Out]): PullFlowBuilder[Out] = {
    val pipe = sourcePipe
    val filter = new SimpleFilter(pipe, operation, ec)
    new PullFlowBuilder(filter, ec)
  }

  def withJoinFilter[In2, Out](pullFlowBuilder: PullFlowBuilder[In2], operation: (In, In2) => Optional[Out]) : PullFlowBuilder[Out] = {
    val pipe1 = sourcePipe
    val pipe2 = new PullPipe(pullFlowBuilder.source, AssoValues.DefaultDuration)
    val filter = new JoinFilter(pipe1, pipe2, operation, ec)
    new PullFlowBuilder[Out](filter, ec)
  }

  def buildJob(nodeBuilder: PullPipe[In] => EndNode[In]): () => Unit = nodeBuilder(sourcePipe).consumeAll
}

object PullFlowBuilder {
  def build[In](sourceBuilder: ExecutionContext => SourceNode[In]): PullFlowBuilder[In] = buildWithContext(sourceBuilder)(ExecutionContext.global)

  def buildWithContext[In](sourceBuilder: ExecutionContext => SourceNode[In])(ec: ExecutionContext): PullFlowBuilder[In] = new PullFlowBuilder(sourceBuilder(ec), ec)


}



