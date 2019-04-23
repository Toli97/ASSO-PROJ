package asso.pipes

object LongOperations {
  def multiplesFilter(number: Long, dividend: Long): Optional[Long] =
    if (number % dividend == 0)
      Value(number)
    else
      NoValue()

  private def isPrime(num: Long): Boolean = {
    if (num <= 1)
      false
    else if (num == 2)
      true
    else
      !(2 to (num - 1)).exists(x => num % x == 0)
  }

  def primeFilter(number: Long): Optional[Long] =
    if (isPrime(number)) Value(number) else NoValue()
}
