package asso.model.objects


abstract class Message[I]() {
  private var currentState: Int = 0

  def getCurrentState(): Int = {
    return currentState
  }

  def setState(nextState: Int) = currentState = nextState
}

case class Value[I](var value: I) extends Message[I]

case class Eof[I]() extends Message[I]()


