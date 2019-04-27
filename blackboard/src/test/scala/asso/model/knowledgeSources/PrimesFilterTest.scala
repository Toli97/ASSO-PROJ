package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.{Finished, Value, ToFilterPrime}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class PrimesFilterTest extends org.scalatest.FunSuite {
  test("PrimesFilter.execute") {
    val blackboard = new Blackboard[Long]()
    val chain = Vector(ToFilterPrime(), Finished())
    blackboard.addToQueue(new Value(11, chain))
    blackboard.addToQueue(new Value(49, chain))
    blackboard.addToQueue(new Value(4, chain))
    blackboard.addToQueue(new Value(10, chain))
    val primesFilter = FilterFactory.buildPrimesFilter(blackboard)
    Await.result(primesFilter.execute(), Duration.Inf)
    assert(blackboard.getQueue(Finished()).size() === 1)
    val first = blackboard.getQueue(Finished()).peek().asInstanceOf[Value[Long]]
    assert(first.value === 11)
  }
}
