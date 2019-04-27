package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.{Finished, Value, ToFilterMultiples}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MultiplesFilterTest extends org.scalatest.FunSuite {
  test("MultiplesFilter.execute") {
    val blackboard = new Blackboard[Long]()
    val chain = Vector(ToFilterMultiples(), Finished())
    blackboard.addToQueue(new Value(11, chain))
    blackboard.addToQueue(new Value(4, chain))
    blackboard.addToQueue(new Value(10, chain))
    blackboard.addToQueue(new Value(49, chain))
    val multiplesFilter = FilterFactory.buildMultiplesFilter(blackboard)
    Await.result(multiplesFilter.execute(), Duration.Inf)
    assert(blackboard.getQueue(Finished()).size() === 2)
    val first = blackboard.getQueue(Finished()).peek().asInstanceOf[Value[Long]]
    assert(first.value === 11)
  }
}
