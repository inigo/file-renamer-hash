package net.surguy.filerenamer

import org.specs2.mutable.SpecificationWithJUnit
import java.io.File
import java.nio.file.Paths
import com.google.common.io.Files
import com.google.common.base.Charsets
import scala.collection.JavaConversions._

class HashRecorderTest extends SpecificationWithJUnit {

  val recorder = new HashRecorder()

  "Working out a relative path" should {
    "give reasonable results when the second path is a subdirectory of the first" in {
      recorder.relativePath(new File("/tmp/root/"), new File("/tmp/root/subdir/one.txt")) mustEqual Paths.get("subdir/one.txt")
    }
  }

  "Processing a directory" should {
    "output a properties file" in {
      val outputFile = File.createTempFile("testhash", ".properties")
      recorder.processAll(new File("src/test/resources"), outputFile)
      val lines = Files.readLines(outputFile, Charsets.UTF_8)
      println(lines.mkString("\n"))
      lines(0) mustEqual "770982747963851d3c3691201ff0d0eb=subdir\\test2.txt"
      lines(1) mustEqual "2e5e1b29fcc9cff2b6ec4e7e0fb02ea8=test.txt"
    }
  }

}
