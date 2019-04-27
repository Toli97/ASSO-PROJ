package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.{Finished, LongWrapper, ToFilterPrime}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class PrimesFilterTest extends org.scalatest.FunSuite {
  test("PrimesFilter.execute") {
    val blackboard = new Blackboard()
    val chain = Vector(ToFilterPrime(), Finished())
    blackboard.addToQueue(new LongWrapper(11, chain))
    blackboard.addToQueue(new LongWrapper(49, chain))
    blackboard.addToQueue(new LongWrapper(4, chain))
    blackboard.addToQueue(new LongWrapper(10, chain))
    val primesFilter = new PrimesFilter(blackboard)
    Await.result(primesFilter.execute(), Duration.Inf)
    assert(blackboard.getQueue(Finished()).size() === 1)
    assert(blackboard.getQueue(Finished()).peek().value === 11)
  }
}
