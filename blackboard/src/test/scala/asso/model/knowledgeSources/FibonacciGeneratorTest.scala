package asso.model.knowledgeSources

import asso.model.{Blackboard, Controller}
import asso.model.objects.ToFilterMultiples
import scala.concurrent.duration._

import scala.concurrent.Await

class FibonacciGeneratorTest extends org.scalatest.FunSuite {


  test("FibonacciGenerator.execute") {
    val blackboard = new Blackboard()
    val controller = new Controller(blackboard)
    val fibonacciGenerator = new FibonacciGenerator(blackboard, ToFilterMultiples())
    assert(fibonacciGenerator.isEnabled === true)
    controller.addKnowledgeSource(fibonacciGenerator)
    Await.result(controller.execute(), Duration.Inf)
    assert(fibonacciGenerator.counter > 1)
    assert(blackboard.objects.length > 0)
  }
}
