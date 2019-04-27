package asso.model.knowledgeSources

import asso.model.Blackboard
import asso.model.objects.{ToFilterMultiples, ToFilterPrime}

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

  def buildPrimesFilter(blackboard: Blackboard[Long]): ConditionFilter[Long] = {
    return new ConditionFilter[Long](blackboard, ToFilterPrime(), isPrime)
  }

  def buildMultiplesFilter(blackboard: Blackboard[Long]): ConditionFilter[Long] = {
    return new ConditionFilter[Long](blackboard, ToFilterMultiples(), isNotMultiple)
  }
}
