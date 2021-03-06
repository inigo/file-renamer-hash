package net.surguy.filerenamer

import java.io._
import org.apache.commons.io.FileUtils
import java.nio.file.Paths
import scala.collection.JavaConversions._

/**
 * Go through a directory and write out a lookup of the filenames and their hashes.
 */
class HashRecorder() {
  def processAll(startDir: File, outputFile: File) {
    val allFiles = FileUtils.listFiles(startDir, null, true)
    val out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)))
    for (file <- allFiles) {
      val hash = processFile(file, startDir)
      out.println(hash.toProperty)
    }
    out.close()
  }

  private[filerenamer] def processFile(file: File, baseDir: File): HashedFile = HashedFile(relativePath(baseDir, file), HashedFile.hash(file))
  private[filerenamer] def relativePath(baseDir: File, file: File) = Paths.get(baseDir.getAbsolutePath).relativize(Paths.get(file.getAbsolutePath))
}
