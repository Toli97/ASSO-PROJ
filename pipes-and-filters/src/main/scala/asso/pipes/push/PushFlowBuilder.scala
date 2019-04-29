package asso.pipes.push

import asso.pipes.Optional

class PushFlowBuilder[In] private(private val source: MessageProducer[In]) {

  private def sourcePipe = new PushPipe(source)

  def withSimpleFilter[Out](operation: In => Optional[Out]): PushFlowBuilder[Out] = {
    val pipe = sourcePipe
    val filter = new SimpleFilter(pipe, operation)
    new PushFlowBuilder(filter)
  }

  def withJoinFilter[In2, Out](pushFlowBuilder: PushFlowBuilder[In2], operation: (In, In2) => Optional[Out]) : PushFlowBuilder[Out] = {
    val pipe1 = sourcePipe
    val pipe2 = new PushPipe(pushFlowBuilder.source)
    val filter = new JoinFilter(pipe1, pipe2, operation)
    new PushFlowBuilder[Out](filter)
  }

  def buildJob(nodeBuilder: PushPipe[In] => EndNode[In]): () => Unit = nodeBuilder(sourcePipe).consumeAll

}

object PushFlowBuilder {
  def build[In](source: SourceNode[In]): PushFlowBuilder[In] = new PushFlowBuilder(source)
}



