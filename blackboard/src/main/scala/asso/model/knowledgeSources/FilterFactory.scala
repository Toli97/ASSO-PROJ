package asso.model.knowledgeSources

object FilterFactory {

  private def isPrime(n: Long): Boolean = {
    if (n < 2) return false
    val ceil: Long = Math.floor(Math.sqrt(n)).asInstanceOf[Long]
    for (i <- 2L to ceil) {
      if (n % i == 0) return false
    }
    return true
  }

  private def isNotMultiple(n: Long): Boolean = {
    val multiplesList = List(2,3,5)
    return multiplesList.filter(l => n % l == 0).length == 0
  }

  def buildPrimesFilter(): ConditionFilter[Long] = {
    return new ConditionFilter[Long](isPrime)
  }

  def buildMultiplesFilter(): ConditionFilter[Long] = {
    return new ConditionFilter[Long](isNotMultiple)
  }
}
