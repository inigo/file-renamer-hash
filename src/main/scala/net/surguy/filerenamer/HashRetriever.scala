package net.surguy.filerenamer

import com.google.common.io.{Files, CharStreams, InputSupplier}
import java.io._
import scala.collection.JavaConversions._
import com.google.common.collect.ArrayListMultimap
import java.nio.file.Path
import org.apache.commons.io.FileUtils
import java.util

/**
 * Go through a directory and rename each of the files in it based on their hash and a lookup previously
 * recorded by `HashRecorder`.
 *
 * Duplicate files will be copied to all the relevant paths - we assume that if two files have the same hash,
 * then they are identical (hash collisions are very unlikely). The most likely cause for dupes will be empty files.
 */
class HashRetriever[T <: Reader](private val lookupStream: InputSupplier[T], baseOutputDir: Path) {

  private val lookup = createLookup(lookupStream)

  private[filerenamer] def createLookup(supplier: InputSupplier[T]) = {
    val lines = CharStreams.readLines(lookupStream)
    val lookupMap = ArrayListMultimap.create[String, Path]()
    var count = 0
    for (line <- lines) {
      val hashedFile = HashedFile.fromProperty(line)
      lookupMap.put(hashedFile.hash, hashedFile.path)
      count = count + 1
      if (count % 5000 == 0) print(".")
    }
    println("\nLookup loaded")
    lookupMap
  }

  def processAll(startDir: File) {
    val allFiles = FileUtils.listFiles(startDir, null, true)
    allFiles.map(processFile)
  }

  private[filerenamer] def processFile(file: File) = {
    val hash = HashedFile.hash(file)
    val outputPaths = lookup.get(hash)
    copyToPaths(file, outputPaths)
  }

  private[filerenamer] def copyToPaths(sourceFile: File, outputPaths: util.List[Path]) {
    var count = 0
    for (path <- outputPaths) {
      val newFile = baseOutputDir.resolve(path).toFile
      if (! newFile.exists()) {
        newFile.getParentFile.mkdirs()
        Files.copy(sourceFile, newFile)
      }
      count = count + 1
      if (count % 5000 == 0) print(".")
    }
  }

}
