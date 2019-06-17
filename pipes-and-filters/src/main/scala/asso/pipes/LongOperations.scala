package asso.pipes

import scala.collection.mutable.Queue

object LongOperations {
  def isMultipleFilter(number: Long, dividend: Long): Optional[Long] =
    if (number % dividend == 0) {
      Value(number)
    }
    else
      NoValue()

  def isNotMultipleFilter(number: Long, dividend: Long): Optional[Long] =
    if (number % dividend != 0)
      Value(number)
    else
      NoValue()

  def isPrime(n: Long): Boolean = {
    if (n < 2) return false
    val ceil: Long = Math.floor(Math.sqrt(n)).asInstanceOf[Long]
    var i: Long = 2L
    while (i < ceil) {
      if (n % i == 0) {
        return false
      }
      i+=1
    }
    true
  }

  def primeFilter(number: Long): Optional[Long] =
    if (isPrime(number)) Value(number) else NoValue()

}
