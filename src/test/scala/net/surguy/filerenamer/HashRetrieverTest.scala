package net.surguy.filerenamer

import org.specs2.mutable.SpecificationWithJUnit
import com.google.common.io.{CharStreams, Files}
import java.nio.file.Paths
import java.io.File
import com.google.common.base.Charsets

class HashRetrieverTest extends SpecificationWithJUnit{

  "copying a file based on its hash" should {
    "produce multiple identical output files" in {
      val outDir = Files.createTempDir()
      val testLookup = "2e5e1b29fcc9cff2b6ec4e7e0fb02ea8=somePath/someFile.txt\n2e5e1b29fcc9cff2b6ec4e7e0fb02ea8=anotherFile.txt"
      val retriever = new HashRetriever( CharStreams.newReaderSupplier(testLookup), Paths.get(outDir.getAbsolutePath) )
      retriever.processFile(new File("src/test/resources/test.txt")) must haveLength(2)
      val expectedOutput = new File(new File(outDir, "somePath"), "someFile.txt")
      expectedOutput must exist
      Files.toString(expectedOutput, Charsets.UTF_8) mustEqual "This is some test text."
      val expectedOutput2 = new File(outDir, "anotherFile.txt")
      expectedOutput2 must exist
      Files.toString(expectedOutput2, Charsets.UTF_8) mustEqual "This is some test text."
    }
    "produce no output for files without matching hashes" in {
      val outDir = Files.createTempDir()
      val retriever = new HashRetriever( CharStreams.newReaderSupplier(""), Paths.get(outDir.getAbsolutePath) )
      retriever.processFile(new File("src/test/resources/test.txt")) must haveLength(0)
      outDir.listFiles() must haveLength(0)
    }
  }
}
