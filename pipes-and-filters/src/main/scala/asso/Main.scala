package asso

object Main {
  def main(args: Array[String]): Unit = {
    if (args.length == 0) {
      throw new IllegalArgumentException("Invalid usage")
    }

    val command = args(0)
    val arguments = args.drop(1)
    command match {
      case "pull" => {
        if (arguments.length == 0) {
          PullJobFactory.buildConsoleJob()()
        } else if (arguments.length != 5) {
          throw new IllegalArgumentException("must specify one output file as first argument and 4 input files as subsequent arguments")
        } else {
          val outPath = arguments(0)
          val inPath1 = arguments(1)
          val inPath2 = arguments(2)
          val inPath3 = arguments(3)
          val inPath4 = arguments(4)
          PullJobFactory.buildAlgorithm(outPath, inPath1, inPath2, inPath3, inPath4)()
        }
      }
      case _ => throw new IllegalArgumentException(s"Invalid command: $command")
    }
  }
}

