package asso.benchmark

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

    val times = 1.to(numRuns).map(_ => runJob(job) / 1e6)
    val folded = times.map(time => f"$time%1.2f").mkString(", ")
    println(s"Runs took: $folded ms")

    val avg = times.sorted.foldLeft(0.0)((acc, e) => acc + e) / times.size
    println(f"Run Average: $avg%1.5f ms")

    val median = times(times.size / 2)
    println(f"Run Median: $median%1.5f ms")
  }
}