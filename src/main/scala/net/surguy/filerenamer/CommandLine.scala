package net.surguy.filerenamer

import java.io.File
import com.google.common.io.Files
import com.google.common.base.Charsets
import java.nio.file.Paths

/**
 * Command line interface to invoking HashRecorder or HashRetriever.
 */
object CommandLine {
  val usage = """
    Usage:
      record [dir]
      retrieve [in_dir] [out_dir]
    """

  def main(args: Array[String]) {
    if (args.length == 0) println(usage)

    args.head match {
      case "record" =>
        val startDir = args(1)
        new HashRecorder().processAll(new File(startDir), new File("lookup.properties"))
      case "retrieve" =>
        val lookup = Files.newReaderSupplier(new File("lookup.properties"), Charsets.UTF_8)
        val startDir = new File(args(1))
        val outDir = Paths.get(new File(args(2)).getAbsolutePath)
        new HashRetriever(lookup, outDir).processAll(startDir)
      case _ =>
        println(usage)
    }
  }

}
