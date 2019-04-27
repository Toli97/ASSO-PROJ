package asso

import asso.model.knowledgeSources.{MultiplesFilter, Output, PrimesFilter}
import asso.model.knowledgeSources.generators.{FibonacciGenerator, RandomGenerator}
import asso.model.objects._
import asso.model.{Blackboard, Controller}

import scala.concurrent.Await
import scala.concurrent.duration.Duration


object Main extends App{

  override def main(args: Array[String]): Unit = {
    val blackboard = new Blackboard()
    val controller = new Controller(blackboard)
    //addKnowledgeSources(controller, blackboard)
    test(controller, blackboard)
  }

  def test(controller: Controller, blackboard: Blackboard): Unit = {
    val chain = Vector(ToFilterMultiples(), ToFilterPrime(), ToOutput(), Finished())
    controller.addKnowledgeSource(new FibonacciGenerator(blackboard, chain))
    controller.addKnowledgeSource(new MultiplesFilter(blackboard))
    controller.addKnowledgeSource(new PrimesFilter(blackboard))
    controller.addKnowledgeSource(new Output(blackboard))
    Await.result(controller.execute(), Duration.Inf)
    println("Finished Test")
  }
  /**
    * Add the knowledge sources that will act on the blackboard and are controlled by the controller
    * @param controller
    * @param blackboard
    */
  def addKnowledgeSources(controller: Controller, blackboard: Blackboard): Unit = {
    val chain1 = Vector(ToFilterMultiples(), ToFilterPrime(), ToAdd1(), ToOutput(), Finished())
    val chain2 = Vector(ToSub1(), ToAdd2(), ToOutput(), Finished())
    val chain3 = Vector(ToFilterMultiples(), ToSub2(), ToAdd2(), ToOutput(), Finished())
    val inputGenerator1 = new RandomGenerator(blackboard, chain3)
    val inputGenerator2 = new RandomGenerator(blackboard, chain3)
    val inputGenerator3 = new RandomGenerator(blackboard, chain2)
    val inputGenerator4 = new FibonacciGenerator(blackboard, chain1)
    controller.addKnowledgeSource(inputGenerator1)
    controller.addKnowledgeSource(inputGenerator2)
    controller.addKnowledgeSource(inputGenerator3)
    controller.addKnowledgeSource(inputGenerator4)
    controller.addKnowledgeSource(new MultiplesFilter(blackboard))
    Await.result(controller.execute(), Duration.Inf)
  }
}
