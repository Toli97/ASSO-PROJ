package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.{Finished, LongWrapper, ToFilterMultiples}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MultiplesFilterTest extends org.scalatest.FunSuite {
  test("MultiplesFilter.execute") {
    val blackboard = new Blackboard()
    val chain = Vector(ToFilterMultiples(), Finished())
    blackboard.addToQueue(new LongWrapper(11, chain))
    blackboard.addToQueue(new LongWrapper(4, chain))
    blackboard.addToQueue(new LongWrapper(10, chain))
    blackboard.addToQueue(new LongWrapper(49, chain))
    val multiplesFilter = new MultiplesFilter(blackboard)
    Await.result(multiplesFilter.execute(), Duration.Inf)
    assert(blackboard.getQueue(Finished()).size() === 2)
    assert(blackboard.getQueue(Finished()).peek().value === 11)
  }
}
