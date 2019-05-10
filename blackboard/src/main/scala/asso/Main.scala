package asso

import asso.model.knowledgeSources.{FilterFactory, Output}
import asso.model.knowledgeSources.generators.FibonacciGenerator
import asso.model.{Blackboard, Controller}

object Main extends App{

  override def main(args: Array[String]): Unit = {
    val blackboard = new Blackboard[Long]()
    val controller = new Controller[Long](blackboard)
    test(controller, blackboard)
  }

  def test(controller: Controller[Long], blackboard: Blackboard[Long]): Unit = {
    val k1 = new FibonacciGenerator();
    val k2 = FilterFactory.buildMultiplesFilter();
    val k3 = new Output[Long]();
    k1.chain(k2);
    k2.chain(k3);
    controller.addKnowledgeSources(List(k1, k2, k3));
    controller.execute()
    println("Finished test")
  }

}
