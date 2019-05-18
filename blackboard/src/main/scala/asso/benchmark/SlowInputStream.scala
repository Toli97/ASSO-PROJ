package asso.benchmarking

import java.io.InputStream

/**
  * Decorator for InputStream
  * @param is
  * @param bytePeriodMs timeout between 2 consecutive bytes in milliseconds
  */
class SlowInputStream(is: InputStream, bytePeriodMs: Int) extends InputStream {

  override def read(): Int = {
    Thread.sleep(bytePeriodMs)
    is.read()
  }

  override def available(): Int = is.available()

  override def close(): Unit = is.close()

  override def mark(readLimit: Int): Unit = is.mark(readLimit)

  override def markSupported(): Boolean = is.markSupported()

  override def reset(): Unit = is.reset()

  override def skip(n: Long): Long = is.skip(n)


}
