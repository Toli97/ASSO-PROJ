package asso.model.objects



class MessageTest extends org.scalatest.FunSuite {
  test("Message.getNextStage") {
    val chain1: Vector[ProcessingStage] = Vector(ToFilterMultiples(), ToFilterPrime())
    val o = new Value(10,chain1)
    assert(o.getCurrentStage() == ToFilterMultiples())
    assert(o.getNextStage() == ToFilterPrime())
    o.advanceStage()
    assert(o.getCurrentStage() == ToFilterPrime())
    o.advanceStage()
    assert(o.getCurrentStage() == ToFilterPrime())
  }
}
