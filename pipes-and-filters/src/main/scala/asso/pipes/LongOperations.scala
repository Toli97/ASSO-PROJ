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

  def isPrime(n: Long): Boolean = ! ((2L until (n-1)) exists (n % _ == 0))

  def primeFilter(number: Long): Optional[Long] =
    if (isPrime(number)) Value(number) else NoValue()

}
