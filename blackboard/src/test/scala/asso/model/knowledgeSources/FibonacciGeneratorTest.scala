package asso.model.knowledgeSources


import asso.model.Blackboard
import asso.model.objects.ToFilterMultiples

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class FibonacciGeneratorTest extends org.scalatest.FunSuite {


  test("FibonacciGenerator.execute") {
    val blackboard = new Blackboard()
    val fibonacciGenerator = new FibonacciGenerator(blackboard, ToFilterMultiples())
    assert(fibonacciGenerator.isEnabled === true)
    Await.result(fibonacciGenerator.execute(), Duration.Inf)
    assert(fibonacciGenerator.counter === 70)
    assert(blackboard.objects.length === 70)
    assert(blackboard.objects(0).value === 0)
    assert(blackboard.objects(1).value === 1)
    assert(blackboard.objects(10).value === 55)
    assert(blackboard.objects(69).value === 117669030460994L)
  }
  
}
