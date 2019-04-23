package asso.pipes.pull.numbers

import asso.pipes.Optional
import asso.pipes.pull.{JoinFilter, PullPipe, SimpleFilter}

object LongFilterFactory {
  def buildSimpleFilter(inPipe: PullPipe[Long], operation: Long => Optional[Long]): SimpleFilter[Long, Long] =
    new SimpleFilter(inPipe, operation)

  def buildJoinFilter(inPipe1: PullPipe[Long], inPipe2: PullPipe[Long], operation: (Long, Long) => Optional[Long]): JoinFilter[Long, Long, Long] =
    new JoinFilter(inPipe1, inPipe2, operation)
}


