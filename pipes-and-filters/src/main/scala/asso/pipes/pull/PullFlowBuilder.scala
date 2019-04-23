package asso.pipes.pull

import asso.pipes.Optional

class PullFlowBuilder[In] private (private val source: MessageProducer[In]) {

  def withSimpleFilter[Out](operation: In => Optional[Out]): PullFlowBuilder[Out] = {
    val pipe = new PullPipe(source)
    val filter = new SimpleFilter(pipe, operation)
    new PullFlowBuilder(filter)
  }

  def withJoinFilter[In2, Out](pullFlowBuilder: PullFlowBuilder[In2], operation: (In, In2) => Optional[Out]) : PullFlowBuilder[Out] = {
    val pipe1 = new PullPipe(source)
    val pipe2 = new PullPipe(pullFlowBuilder.source)
    val filter = new JoinFilter(pipe1, pipe2, operation)
    new PullFlowBuilder[Out](filter)
  }
}

object PullFlowBuilder {
  def build[In](source: SourceNode[In]): PullFlowBuilder[In] = new PullFlowBuilder(source)
}



