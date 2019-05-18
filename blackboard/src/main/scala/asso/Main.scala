package asso

import asso.knowledgeSources.{FilterFactory, JoinFilter, OutputFactory, ProducerFactory}

object Main extends App {

  override def main(args: Array[String]): Unit = {
    val blackboard = new Blackboard[Long]()
    val controller = new Controller[Long](blackboard)
    scenario1(controller, blackboard)
    //simpleScenario(controller, blackboard)
    //testJoinFilter(controller, blackboard)
  }

  def simpleScenario(controller: Controller[Long], blackboard: Blackboard[Long]) = {
    val generator = ProducerFactory.fromFile("fibonacci.in")
    val filter = FilterFactory.buildMultiplesFilter()
    val output = OutputFactory.toFile("simpleScenario.out")
    val ksList = List(generator, filter, output)
    controller.addKnowledgeSources(ksList)

    generator.chain(filter).chain(output)
    controller.execute()
    println("Simple scenario finished")
  }

  def testJoinFilter(controller: Controller[Long], blackboard: Blackboard[Long]) = {
    val generator1 = ProducerFactory.fromFile("fibonacci.in")
    val generator2 = ProducerFactory.fromFile("rand1.in")
    val filter = FilterFactory.buildJoinMultiplesFilter()
    val output = OutputFactory.toFile("testJoinFilter.out")
    val ksList = List(generator1, generator2, filter, output)
    controller.addKnowledgeSources(ksList)
    generator1.chain(filter)
    generator2.chain(filter).chain(output)
    controller.execute()
    println("Test Join filter finished")
  }

  def scenario1(controller: Controller[Long], blackboard: Blackboard[Long]): Unit = {
    val fibGen = ProducerFactory.fromFile("fibonacci.in")
    val randGen1 = ProducerFactory.fromFile("rand1.in")
    val randGen2 = ProducerFactory.fromFile("rand2.in")
    val randGen3 = ProducerFactory.fromFile("rand3.in")
    val multFilter = FilterFactory.buildMultiplesFilter()
    val primeFilter = FilterFactory.buildMultiplesFilter()
    val joinMultFilter = FilterFactory.buildJoinMultiplesFilter()
    val sub = new JoinFilter[Long]((a, b) => a - b)
    val add = new JoinFilter[Long]((a, b) => a + b)
    val kss = List(fibGen, randGen1, randGen2, randGen3, multFilter, primeFilter, joinMultFilter, sub, add)
    controller.addKnowledgeSources(kss)

    fibGen.chain(multFilter).chain(primeFilter).chain(add)
    randGen1.chain(sub).chain(add)
    randGen2.chain(joinMultFilter).chain(sub)
    randGen3.chain(joinMultFilter)

    controller.execute()
    println("Finished test")
  }

}
