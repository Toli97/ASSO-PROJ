package asso

import asso.benchmark.Benchmarker
import asso.knowledgeSources.{FilterFactory, OperationFactory, OutputFactory, ProducerFactory}

object Main extends App {

  override def main(args: Array[String]): Unit = {
    val blackboard = new Blackboard[Long]()
    val controller = new Controller[Long](blackboard)
    //scenario1(controller, blackboard)
    //simpleScenario(controller, blackboard)
    //testJoinFilter(controller, blackboard)
    //testOperationFilter(controller, blackboard)
    //Benchmarker.bench(100, () => simpleScenario(controller, blackboard))
    //Benchmarker.bench(10, () => scenario1(controller, blackboard))
    Benchmarker.bench(3, () => scenario2(controller, blackboard))
  }

  def simpleScenario(controller: Controller[Long], blackboard: Blackboard[Long]) = {
    val generator = ProducerFactory.fromFile("fib.in")
    val filter = FilterFactory.buildMultiplesFilter()
    val output = OutputFactory.toFile("simpleScenario.out")
    val ksList = List(generator, filter, output)
    controller.addKnowledgeSources(ksList)

    generator.chain(filter).chain(output)
    controller.execute()
    println("Simple scenario finished")
  }

  def testJoinMultiplesFilter(controller: Controller[Long], blackboard: Blackboard[Long]) = {
    val generator1 = ProducerFactory.fromFile("fib.in")
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

  def testOperationFilter(controller: Controller[Long], blackboard: Blackboard[Long]) = {
    val rand1 = ProducerFactory.fromFile("smallRand1.in")
    val rand2 = ProducerFactory.fromFile("smallRand2.in")
    val add = OperationFactory.buildAdd()
    val sink = OutputFactory.toFile("testOperationFilter.out")
    val kss = List(rand1,rand2,add,sink)
    controller.addKnowledgeSources(kss)
    rand1.chain(add)
    rand2.chain(add).chain(sink)
    controller.execute()
    println("Test Operation filter finished")
  }

  def scenario1(controller: Controller[Long], blackboard: Blackboard[Long]): Unit = {
    val fibGen = ProducerFactory.fromFile("fib.in")
    val randGen1 = ProducerFactory.fromFile("rand1.in")
    val randGen2 = ProducerFactory.fromFile("rand2.in")
    val randGen3 = ProducerFactory.fromFile("rand3.in")
    val multFilter = FilterFactory.buildMultiplesFilter()
    val primeFilter = FilterFactory.buildPrimesFilter()
    val joinMultFilter = FilterFactory.buildJoinMultiplesFilter()
    val sub = OperationFactory.buildSub()
    val add = OperationFactory.buildAdd()
    val sink = OutputFactory.toFile("scenario1.out")
    val kss = List(fibGen, randGen1, randGen2, randGen3, multFilter, primeFilter, joinMultFilter, sub, add, sink)
    controller.addKnowledgeSources(kss)

    fibGen.chain(multFilter).chain(primeFilter).chain(add).chain(sink)
    randGen1.chain(sub).chain(add)
    randGen2.chain(joinMultFilter).chain(sub)
    randGen3.chain(joinMultFilter)

    controller.execute()
    println("Finished scenario 1")
  }

  def scenario2(controller: Controller[Long], blackboard: Blackboard[Long]) = {
    val fibGen = ProducerFactory.slowFromFile("fib.in", 100)
    val randGen1 = ProducerFactory.fromFile("smallRand1.in")
    val randGen2 = ProducerFactory.slowFromFile("smallRand2.in", 200)
    val randGen3 = ProducerFactory.fromFile("smallRand1.in")
    val multFilter = FilterFactory.buildMultiplesFilter()
    val primeFilter = FilterFactory.buildPrimesFilter()
    val joinMultFilter = FilterFactory.buildJoinMultiplesFilter()
    val sub = OperationFactory.buildSub()
    val add = OperationFactory.buildAdd()
    val sink = OutputFactory.toFile("scenario2.out")
    val kss = List(fibGen, randGen1, randGen2, randGen3, multFilter, primeFilter, joinMultFilter, sub, add, sink)
    controller.addKnowledgeSources(kss)

    fibGen.chain(multFilter).chain(primeFilter).chain(add).chain(sink)
    randGen1.chain(sub).chain(add)
    randGen2.chain(joinMultFilter).chain(sub)
    randGen3.chain(joinMultFilter)

    controller.execute()
    println("Finished scenario 2")
  }

}
