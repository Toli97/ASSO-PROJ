package asso

import asso.benchmark.Benchmarker

object Main {

  def main(args: Array[String]): Unit = {
    try {
      testableMain(args)
      sys.exit(0)
    } catch {
      case e: Throwable => {
        System.err.println(e.getMessage)
        sys.exit(1)
      }
    }
  }

  def testableMain(args: Array[String]): Unit = {
    assertNotEmpty(args)

    if (args(0) == "bench") {
      runBenchmark(args.drop(1))
    } else {
      runCommand(args)
    }
  }

  private def runBenchmark(args: Array[String]): Unit = {
    assertNotEmpty(args)

    val numRuns = args(0).toInt
    val commandArgs = args.drop(1)
    Benchmarker.bench(numRuns, () => runCommand(commandArgs))
  }

  private def runCommand(args: Array[String]): Unit = {
    assertNotEmpty(args)

    if (args.length == 5) {
      runFast(args)
    } else if (args.length == 7) {
      runSlow(args)
    } else {
      printUsage()
    }
  }

  private def printUsage() = {
    println("Arguments: bench? numRuns? outFile inFile1 inFile2 inFile3 inFile4 byteRate1? byteRate2? ")
    println("Bench and numRuns are used to run benchmarks")
    println("If byteRate1 and byteRate2 are set, the slow algorithm will be used")
  }

  private def runFast(arguments: Array[String]): Unit = {
    ScenarioFactory.runFastAlgorithm(arguments(0), arguments(1), arguments(2), arguments(3), arguments(4))
  }

  private def runSlow(arguments: Array[String]): Unit = {
    ScenarioFactory.runSlowAlgorithm(arguments(0), arguments(1), arguments(2), arguments(3), arguments(4), arguments(5).toInt, arguments(6).toInt)
  }

  private def assertNotEmpty(args: Array[String]) = if (args.length == 0) {
    printUsage()
    throw new IllegalArgumentException("Invalid usage")
  }


}
