package asso

import asso.benchmarking.Benchmarker
import asso.pipes.PullJobFactory
import asso.pipes.PushJobFactory

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

    val command = args(0)
    val arguments = args.drop(1)
    command match {
      case "pull" => runSimplePull(arguments)
      case "slowPull" => runSlowPull(arguments)
      case "push" => runPush(arguments)
      case _ => throw new IllegalArgumentException(s"Invalid command: $command")
    }
  }

  private def runSimplePull(arguments: Array[String]): Unit = if (arguments.length == 0) {
    PullJobFactory.buildConsoleJob()()
  } else if (arguments.length != 5) {
    throw new IllegalArgumentException("must specify one output file as first argument and 4 input files as subsequent arguments")
  } else {
    PullJobFactory.buildAlgorithm(arguments(0), arguments(1), arguments(2), arguments(3), arguments(4))()
  }

  private def runSlowPull(arguments: Array[String]): Unit = if (arguments.length == 0) {
    PullJobFactory.buildConsoleJob()()
  } else if (arguments.length != 5) {
    throw new IllegalArgumentException("must specify one output file as first argument and 4 input files as subsequent arguments")
  } else {
    PullJobFactory.buildSlowAlgorithm(arguments(0), arguments(1), arguments(2), arguments(3), arguments(4))()
  }

  private def runPush(arguments: Array[String]): Unit = if (arguments.length == 0) {
    PushJobFactory.buildConsoleJob()()
  } else if (arguments.length != 5) {
    throw new IllegalArgumentException("must specify one output file as first argument and 4 input files as subsequent arguments")
  } else {
    PushJobFactory.buildAlgorithm(arguments(0), arguments(1), arguments(2), arguments(3), arguments(4))()
  }

  private def assertNotEmpty(args: Array[String]) = if (args.length == 0) {
    throw new IllegalArgumentException("Invalid usage")
  }
}

