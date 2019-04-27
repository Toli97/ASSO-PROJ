package asso

import asso.model.knowledgeSources.generators.FibonacciGenerator
import asso.model.knowledgeSources.{FilterFactory, Output}
import asso.model.objects._
import asso.model.{Blackboard, Controller}

import scala.concurrent.Await
import scala.concurrent.duration.Duration


object Main extends App{

  override def main(args: Array[String]): Unit = {
    val blackboard = new Blackboard[Long]()
    val controller = new Controller[Long](blackboard)
    //addKnowledgeSources(controller, blackboard)
    test(controller, blackboard)
  }

  def test(controller: Controller[Long], blackboard: Blackboard[Long]): Unit = {
    val chain = Vector(ToFilterMultiples(), ToFilterPrime(), ToOutput(), Finished())
    controller.addKnowledgeSource(new FibonacciGenerator(blackboard, chain))
    controller.addKnowledgeSource(FilterFactory.buildMultiplesFilter(blackboard))
    controller.addKnowledgeSource(FilterFactory.buildPrimesFilter(blackboard))
    controller.addKnowledgeSource(new Output(blackboard))
    Await.result(controller.execute(), Duration.Inf)
    println("Finished Test")
  }

}
