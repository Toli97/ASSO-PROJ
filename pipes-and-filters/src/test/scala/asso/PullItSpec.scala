package asso

import java.io.{File, PrintWriter}

import org.scalatest._

import scala.io.Source

object RandomName {
  private var curr = 0

  def nextName: String = {
    curr += 1;
    s"ASSO-Proj-test-$curr"
  }
}


class ItSpec extends FlatSpec with Matchers {
  def createFile(body: String = ""): String = {
    val file = File.createTempFile(RandomName.nextName, "txt")
    val printer = new PrintWriter(file)
    printer.print(body)
    printer.close()
    file.toString
  }

  def readFileAsNums(filepath: String): Seq[Long] = Source.fromFile(filepath).mkString
    .split("\\s+")
    .map(word => word.toLong)


  "The algorithm" should "should work" in {
    val in1 = createFile("2 3 8 4 7 10 11")
    val in2 = createFile("10 5 5")
    val in3 = createFile("2 4 9 12 11 15 1")
    val in4 = createFile("3 2 3 10 2 5")
    val out = createFile()
    val expectedNums = Seq(8, -1, -3)

    Main.main(Array("pull", out, in1, in2, in3, in4))

    val actualNums = readFileAsNums(out)
    expectedNums shouldEqual actualNums
  }



}
