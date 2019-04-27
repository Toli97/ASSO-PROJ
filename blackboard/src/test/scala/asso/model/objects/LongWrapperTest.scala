package asso.model.objects



class LongWrapperTest extends org.scalatest.FunSuite {
  test("LongWrapper.getNextStage") {
    val chain1: Vector[ProcessingStage] = Vector(ToFilterMultiples(), ToFilterPrime())
    val o = new LongWrapper(10,chain1)
    assert(o.getCurrentStage() == ToFilterMultiples())
    assert(o.getNextStage() == ToFilterPrime())
    o.advanceStage()
    assert(o.getCurrentStage() == ToFilterPrime())
    o.advanceStage()
    assert(o.getCurrentStage() == ToFilterPrime())
  }
}
