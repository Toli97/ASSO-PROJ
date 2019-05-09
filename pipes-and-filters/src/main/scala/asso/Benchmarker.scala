package asso

object Benchmarker {

  private def runJob(job: () => Unit): Long = {
    val start = System.nanoTime()
    job()
    val end = System.nanoTime()
    end - start
  }

  /**
    *
    * @param job should be stateless
    */
  def bench(numRuns: Int, job: () => Unit): Unit = {
    if (numRuns < 1) {
      throw new IllegalArgumentException("Num run must be positive")
    }

    val times = 1.to(numRuns).map(i => (i, runJob(job) / 1e6))
    times.foreach(pair => println(s"Run ${pair._1} took: ${pair._2} ms"))

    val sorted = times.map(pair => pair._2).sorted
    val avg = sorted.foldLeft(0.0)((acc, e) => acc + e) / sorted.size

    println(s"Run Average: $avg ms")
    val median = sorted(sorted.size / 2)
    println(s"Run Median: $median ms")
  }
}
