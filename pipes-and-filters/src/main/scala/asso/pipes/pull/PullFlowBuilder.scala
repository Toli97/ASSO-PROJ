package asso.pipes.pull

import asso.pipes.{AssoValues, Optional}

class PullFlowBuilder[In] private (private val source: MessageProducer[In]) {

  private def sourcePipe = new PullPipe(source, AssoValues.DefaultDuration)

  def withSimpleFilter[Out](operation: In => Optional[Out]): PullFlowBuilder[Out] = {
    val pipe = sourcePipe
    val filter = new SimpleFilter(pipe, operation)
    new PullFlowBuilder(filter)
  }

  def withJoinFilter[In2, Out](pullFlowBuilder: PullFlowBuilder[In2], operation: (In, In2) => Optional[Out]) : PullFlowBuilder[Out] = {
    val pipe1 = sourcePipe
    val pipe2 = new PullPipe(pullFlowBuilder.source, AssoValues.DefaultDuration)
    val filter = new JoinFilter(pipe1, pipe2, operation)
    new PullFlowBuilder[Out](filter)
  }

  def buildJob(nodeBuilder: PullPipe[In] => EndNode[In]): () => Unit = nodeBuilder(sourcePipe).consumeAll

}

object PullFlowBuilder {
  def build[In](source: SourceNode[In]): PullFlowBuilder[In] = new PullFlowBuilder(source)


}



