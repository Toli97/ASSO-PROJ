package asso

import asso.benchmark.Benchmarker
import asso.knowledgeSources.{FilterFactory, OperationFactory, OutputFactory, ProducerFactory}

object Main extends App {

  override def main(args: Array[String]): Unit = {
    //Benchmarker.bench(100, () => scenario1())
    Benchmarker.bench(10, () => scenario2())
  }


  def scenario1(): Unit = {
    val blackboard = new Blackboard[Long]()
    val controller = new Controller[Long](blackboard)
    val fibGen = ProducerFactory.fromFile("fib.in")
    val collatzGen = ProducerFactory.fromFile("collatz.in")
    val primesGen = ProducerFactory.fromFile("primes.in")
    val randGen = ProducerFactory.fromFile("rand.in")
    val multFilter = FilterFactory.buildMultiplesFilter()
    val primeFilter = FilterFactory.buildPrimesFilter()
    val joinMultFilter = FilterFactory.buildJoinMultiplesFilter()
    val sub = OperationFactory.buildSub()
    val add = OperationFactory.buildAdd()
    val sink = OutputFactory.toFile("scenario1.out")
    val kss = List(fibGen, collatzGen, primesGen, randGen, multFilter, primeFilter, joinMultFilter, sub, add, sink)
    controller.addKnowledgeSources(kss)

    fibGen.chain(multFilter).chain(primeFilter).chain(add).chain(sink)
    collatzGen.chain(sub).chain(add)
    primesGen.chain(joinMultFilter).chain(sub)
    randGen.chain(joinMultFilter)

    controller.execute()
    println("Finished scenario 1")
  }

  def scenario2() = {
    val blackboard = new Blackboard[Long]()
    val controller = new Controller[Long](blackboard)
    val fibGen = ProducerFactory.slowFromFile("fib.in", 1000)
    val collatzGen = ProducerFactory.fromFile("collatz.in")
    val primesGen = ProducerFactory.slowFromFile("primes.in", 1000)
    val randGen = ProducerFactory.fromFile("rand.in")
    val multFilter = FilterFactory.buildMultiplesFilter()
    val primeFilter = FilterFactory.buildPrimesFilter()
    val joinMultFilter = FilterFactory.buildJoinMultiplesFilter()
    val sub = OperationFactory.buildSub()
    val add = OperationFactory.buildAdd()
    val sink = OutputFactory.toFile("scenario2.out")
    val kss = List(fibGen, collatzGen, primesGen, randGen, multFilter, primeFilter, joinMultFilter, sub, add, sink)
    controller.addKnowledgeSources(kss)

    fibGen.chain(multFilter).chain(primeFilter).chain(add).chain(sink)
    collatzGen.chain(sub).chain(add)
    primesGen.chain(joinMultFilter).chain(sub)
    randGen.chain(joinMultFilter)

    controller.execute()
    println("Finished scenario 2")
  }

  def simpleScenario() = {
    val blackboard = new Blackboard[Long]()
    val controller = new Controller[Long](blackboard)
    val generator = ProducerFactory.fromFile("fib.in")
    val filter = FilterFactory.buildMultiplesFilter()
    val output = OutputFactory.toFile("simpleScenario.out")
    val ksList = List(generator, filter, output)
    controller.addKnowledgeSources(ksList)

    generator.chain(filter).chain(output)
    controller.execute()
    println("Simple scenario finished")
  }

  def testJoinMultiplesFilter() = {
    val blackboard = new Blackboard[Long]()
    val controller = new Controller[Long](blackboard)
    val generator1 = ProducerFactory.fromFile("fib.in")
    val generator2 = ProducerFactory.fromFile("rand.in")
    val filter = FilterFactory.buildJoinMultiplesFilter()
    val output = OutputFactory.toFile("testJoinFilter.out")
    val ksList = List(generator1, generator2, filter, output)
    controller.addKnowledgeSources(ksList)
    generator1.chain(filter)
    generator2.chain(filter).chain(output)
    controller.execute()
    println("Test Join filter finished")
  }

  def testOperationFilter() = {
    val blackboard = new Blackboard[Long]()
    val controller = new Controller[Long](blackboard)
    val rand1 = ProducerFactory.fromFile("fib.in")
    val rand2 = ProducerFactory.fromFile("fib.in")
    val add = OperationFactory.buildAdd()
    val sink = OutputFactory.toFile("testOperationFilter.out")
    val kss = List(rand1,rand2,add,sink)
    controller.addKnowledgeSources(kss)
    rand1.chain(add)
    rand2.chain(add).chain(sink)
    controller.execute()
    println("Test Operation filter finished")
  }


}
