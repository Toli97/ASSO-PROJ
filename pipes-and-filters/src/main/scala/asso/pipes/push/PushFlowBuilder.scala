package asso.pipes.push

import asso.pipes.{AssoValues, Optional}
import scala.concurrent.ExecutionContext


class PushFlowBuilder[In] private (private val source: MessageProducer[In], private val ec: ExecutionContext) {

  private def sourcePipe = new PushPipe(source, AssoValues.DefaultDuration)

  def withSimpleFilter[Out](operation: In => Optional[Out]): PushFlowBuilder[Out] = {
    val pipe = sourcePipe
    val filter = new SimpleFilter(pipe, operation, ec)
    new PushFlowBuilder(filter, ec)
  }

  def withJoinFilter[In2, Out](pushFlowBuilder: PushFlowBuilder[In2], operation: (In, In2) => Optional[Out]) : PushFlowBuilder[Out] = {
    val pipe1 = sourcePipe
    val pipe2 = new PushPipe(pushFlowBuilder.source, AssoValues.DefaultDuration)
    val filter = new JoinFilter(pipe1, pipe2, operation, ec)
    new PushFlowBuilder[Out](filter, ec)
  }

  def buildJob(nodeBuilder: PushPipe[In] => EndNode[In]): () => Unit = nodeBuilder(sourcePipe).consumeAll
}

object PushFlowBuilder {
  def build[In](sourceBuilder: ExecutionContext => SourceNode[In]): PushFlowBuilder[In] = buildWithContext(sourceBuilder)(ExecutionContext.global)

  def buildWithContext[In](sourceBuilder: ExecutionContext => SourceNode[In])(ec: ExecutionContext): PushFlowBuilder[In] = new PushFlowBuilder(sourceBuilder(ec), ec)


}


