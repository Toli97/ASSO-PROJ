package asso

import java.io.{File, FileInputStream, PrintWriter}
import java.util.Scanner

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


  "The algorithm" should "should work in a simple join flow" in {
    val in1 = createFile("1 2 3 4")
    val in2 = createFile("3 2 1 0")
    val out = createFile()
    val expectedNums = Seq(5, 5)

    Main.main(Array("pull", out, in1, in2))

    val actualNums = readFileAsNums(out)
    expectedNums shouldEqual actualNums
  }
}
