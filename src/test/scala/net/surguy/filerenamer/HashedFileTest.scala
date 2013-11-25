package net.surguy.filerenamer

import org.specs2.mutable.SpecificationWithJUnit
import java.nio.file.Paths
import java.io.File

class HashedFileTest extends SpecificationWithJUnit{

  "converting hashes into strings" should {
    val hashedFile = HashedFile(Paths.get("somePath"), "2e5e1b29fcc9cff2b6ec4e7e0fb02ea8")
    val line = "2e5e1b29fcc9cff2b6ec4e7e0fb02ea8=somePath"
    "convert a hash to a string" in { hashedFile.toProperty mustEqual line}
    "convert a string to a hash" in { HashedFile.fromProperty(line) mustEqual hashedFile }
    "convert a hash to a string and back" in { HashedFile.fromProperty(hashedFile.toProperty) mustEqual hashedFile }
  }

  "Hashing a file" should {
    "return a hash that matches an expected value" in {
      HashedFile.hash(new File("src/test/resources/test.txt")) mustEqual "2e5e1b29fcc9cff2b6ec4e7e0fb02ea8"
    }
  }

}
