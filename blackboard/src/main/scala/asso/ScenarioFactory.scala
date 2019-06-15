package asso

import asso.knowledgeSources.{FilterFactory, KnowledgeSource, OperationFactory, OutputFactory, ProducerFactory}

object ScenarioFactory {

  def runFastAlgorithm(outPath: String, input1: String, input2: String, input3: String, input4: String): Unit = {
    val input1KS = ProducerFactory.fromFile(input1)
    val input2KS = ProducerFactory.fromFile(input2)
    val input3KS = ProducerFactory.fromFile(input3)
    val input4KS = ProducerFactory.fromFile(input4)
    val sinkKS = OutputFactory.toFile(outPath)

    runScenario(input1KS, input2KS, input3KS, input4KS, sinkKS)
  }

  def runSlowAlgorithm(outPath: String, input1: String, input2: String, input3: String, input4: String, byteRate1: Int, byteRate2: Int): Unit = {

    val input1KS = ProducerFactory.slowFromFile(input1, byteRate1)
    val input2KS = ProducerFactory.fromFile(input2)
    val input3KS = ProducerFactory.slowFromFile(input3, byteRate2)
    val input4KS = ProducerFactory.fromFile(input4)
    val sinkKS = OutputFactory.toFile(outPath)

    runScenario(input1KS, input2KS, input3KS, input4KS, sinkKS)
  }

  private def runScenario(input1KS: KnowledgeSource[Long], input2KS: KnowledgeSource[Long],
                             input3KS: KnowledgeSource[Long], input4KS: KnowledgeSource[Long],
                             outputKS: KnowledgeSource[Long]) = {
    val blackboard = new Blackboard[Long]()
    val controller = new Controller[Long](blackboard)
    val multiplesFilter = FilterFactory.buildMultiplesFilter()
    val primeFilter = FilterFactory.buildPrimesFilter()
    val joinMultiplesFilter = FilterFactory.buildJoinMultiplesFilter()
    val sub = OperationFactory.buildSub()
    val add = OperationFactory.buildAdd()

    val kss = List(input1KS, input2KS, input3KS, input4KS, multiplesFilter, primeFilter, joinMultiplesFilter, sub, add, outputKS)
    controller.addKnowledgeSources(kss)

    input1KS.chain(multiplesFilter).chain(primeFilter).chain(add).chain(outputKS)
    input2KS.chain(sub).chain(add)
    input3KS.chain(joinMultiplesFilter).chain(sub)
    input4KS.chain(joinMultiplesFilter)

    controller.execute()
    println("Finished scenario")
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
