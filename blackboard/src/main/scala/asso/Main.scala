package asso

import asso.model.knowledgeSources.{FibonacciGenerator, RandomGenerator}
import asso.model.objects.{ToFilterMultiples, ToSub1}
import asso.model.{Blackboard, Controller}


object Main extends App{

  override def main(args: Array[String]): Unit = {
    val blackboard = new Blackboard()
    val controller = new Controller(blackboard)
    addKnowledgeSources(controller, blackboard)
  }

  /**
    * Add the knowledge sources that will act on the blackboard and are controlled by the controller
    * @param controller
    * @param blackboard
    */
  def addKnowledgeSources(controller: Controller, blackboard: Blackboard): Unit = {
    val inputGenerator1 = new RandomGenerator(blackboard, ToFilterMultiples())
    val inputGenerator2 = new RandomGenerator(blackboard, ToFilterMultiples())
    val inputGenerator3 = new RandomGenerator(blackboard, ToSub1())
    val inputGenerator4 = new FibonacciGenerator(blackboard, ToFilterMultiples())
    controller.addKnowledgeSource(inputGenerator1)
    controller.addKnowledgeSource(inputGenerator2)
    controller.addKnowledgeSource(inputGenerator3)
    controller.addKnowledgeSource(inputGenerator4)
  }
}
