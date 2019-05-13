package asso

import asso.model.knowledgeSources.generators.{FibonacciGenerator, RandomGenerator}
import asso.model.knowledgeSources.{FilterFactory, JoinFilter}
import asso.model.{Blackboard, Controller}

object Main extends App {

  override def main(args: Array[String]): Unit = {
    val blackboard = new Blackboard[Long]()
    val controller = new Controller[Long](blackboard)
    scenario1(controller, blackboard)
  }

  def scenario1(controller: Controller[Long], blackboard: Blackboard[Long]): Unit = {
    val kfib = new FibonacciGenerator
    val krand1 = new RandomGenerator
    val krand2 = new RandomGenerator
    val krand3 = new RandomGenerator
    val kmf1 = FilterFactory.buildMultiplesFilter()
    val kmf2 = FilterFactory.buildMultiplesFilter()
    val kpf1 = FilterFactory.buildPrimesFilter()
    val ksub = new JoinFilter[Long]((a, b) => a - b)
    val kadd = new JoinFilter[Long]((a, b) => a + b)

    kfib.chain(kmf1)
    kmf1.chain(kpf1)
    kpf1.chain(kadd)
    krand1.chain(ksub)
    ksub.chain(kadd)
    krand2.chain(kmf2)
    krand3.chain(kmf2)
    kmf2.chain(ksub)
    val kss = List(kfib, krand1, krand2, krand3, kmf1, kmf2, kpf1, ksub, kadd)
    controller.addKnowledgeSources(kss)
    controller.execute()
    println("Finished test")
  }

}
