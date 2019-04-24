package asso.pipes

object LongOperations {
  def multiplesFilter(number: Long, dividend: Long): Optional[Long] =
    if (number % dividend == 0)
      Value(number)
    else
      NoValue()

  private def isPrime(num: Int): Boolean = (num > 1) && !(2 to scala.math.sqrt(num).toInt).exists(x =>num % x == 0)

  def primeFilter(number: Long): Optional[Long] =
    if (isPrime(number.toInt)) Value(number) else NoValue()
}
