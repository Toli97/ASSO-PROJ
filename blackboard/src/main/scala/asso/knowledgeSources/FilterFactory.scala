package asso.knowledgeSources

object FilterFactory {

  private def isPrime(n: Long): Boolean = {
    if (n < 2) return false
    val ceil: Long = Math.floor(Math.sqrt(n)).asInstanceOf[Long]
    var i: Long = 2L;
    while (i < ceil) {
      if (n % i == 0) return false
      i+=1
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

  def buildJoinMultiplesFilter(): JoinConditionFilter[Long] = {
    return new JoinConditionFilter[Long](isNotMultiple)
  }
}
