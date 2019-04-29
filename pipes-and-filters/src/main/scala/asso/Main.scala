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
        } else if (arguments.length != 3) {
          throw new IllegalArgumentException("must specify out file as first and two input file")
        } else {
          val outPath = arguments(0)
          val inPath1 = arguments(1)
          val inPath2 = arguments(2)
          PullJobFactory.buildAlgorithm(outPath, inPath1, inPath2)()
        }
        case "push" => {
          if (arguments.length == 0) {
            PushJobFactory.buildConsoleJob()()
          } else if (arguments.length != 3) {
            throw new IllegalArgumentException("must specify out file as first and two input file")
          } else {
            val outPath = arguments(0)
            val inPath1 = arguments(1)
            val inPath2 = arguments(2)
            PushJobFactory.buildAlgorithm(outPath, inPath1, inPath2)()
          }
      }
      case _ => throw new IllegalArgumentException(s"Invalid command: $command")
    }
  }
}

