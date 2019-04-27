package asso.model.knowledgeSources


import asso.model.Blackboard
import asso.model.knowledgeSources.generators.FibonacciGenerator
import asso.model.objects.{Finished, ToFilterMultiples, Value}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class FibonacciGeneratorTest extends org.scalatest.FunSuite {


  test("FibonacciGenerator.execute") {
    val blackboard = new Blackboard[Long]()
    val chain = Vector(ToFilterMultiples(), Finished())
    val fibonacciGenerator = new FibonacciGenerator(blackboard, chain)
    assert(fibonacciGenerator.isEnabled === true)
    Await.result(fibonacciGenerator.execute(), Duration.Inf)
    assert(fibonacciGenerator.counter === 70)
    val queue = blackboard.getQueue(ToFilterMultiples())
    assert(queue.size() === 70)
    val first = queue.peek().asInstanceOf[Value[Long]]
    assert(first.value === 0)
  }

}
