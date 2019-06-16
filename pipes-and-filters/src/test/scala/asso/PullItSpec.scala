package asso

import java.io.{File, FileNotFoundException, PrintWriter}

import org.scalatest._

import scala.io.Source

object RandomName {
  private var curr = 0

  def nextName: String = {
    curr += 1
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

  def readFileAsNums(filepath: String): Seq[Long] = {
    val str = Source.fromFile(filepath).mkString

    if (str == "") {
      return Seq()
    }

    str.split("\\s+")
      .map(word => word.toLong)
  }

  private val ValidPrimes = "2 3 8 4 7 10 11"
  private val ValidSubs = "10 5 5"
  private val ValidFiltered = "2 4 9 12 11 15 1"
  private val ValidFilter = "3 2 3 10 2 5"
  private val ValidOutputSeq = Seq(8, -1, -2) // manually calculated to be correct

  "The algorithm" should "should work" in {
    val out = createFile()
    Main.testableMain(Array("pull", out, createFile(ValidPrimes), createFile(ValidSubs), createFile(ValidFiltered), createFile(ValidFilter)))
    val actualNums = readFileAsNums(out)
    ValidOutputSeq shouldEqual actualNums
  }

  "The algorithm benchmark" should "should work" in {
    val out = createFile()
    Main.testableMain(Array("bench", "3", "pull", out, createFile(ValidPrimes), createFile(ValidSubs), createFile(ValidFiltered), createFile(ValidFilter)))
    val actualNums = readFileAsNums(out)
    ValidOutputSeq shouldEqual actualNums
  }

  "The algorithm with empty inputs" should "should produce empty output" in {
    val expectedNums = Seq()

    val out = createFile()
    Main.testableMain(Array("pull", out, createFile(), createFile(), createFile(), createFile()))

    val actualNums = readFileAsNums(out)
    expectedNums shouldEqual actualNums
  }

  "An invalid command" should "fail" in {
    val invalidCommand = "ASDFGQWE"
    try {
      Main.testableMain(Array(invalidCommand))
      fail()
    } catch {
      case e: IllegalArgumentException => e.getMessage should include(invalidCommand)
    }
  }

  "An invalid number of commands" should "fail" in {
    try {
      Main.testableMain(Array("pull", createFile(), createFile()))
      fail()
    } catch {
      case e: IllegalArgumentException => e.getMessage should include("must specify one output")
    }
  }

  "Invalid text in a source file" should "fail" in {
    val invalidText = "awwe"
    try {
      Main.testableMain(Array("pull", createFile(), createFile("2"), createFile("2"), createFile("2"), createFile(invalidText)))
      fail()
    } catch {
      case e: NumberFormatException => e.getMessage should include(invalidText)
    }
  }

  "Non existing source file" should "fail" in {
    val invalidSource = createFile() + "QQQQQQQ"
    try {
      Main.testableMain(Array("pull", createFile(), createFile("2"), createFile("2"), createFile("2"), invalidSource))
      fail()
    } catch {
      case e: FileNotFoundException => e.getMessage should include(invalidSource)
    }
  }

}
