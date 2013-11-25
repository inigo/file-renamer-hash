package net.surguy.filerenamer

import java.nio.file.{Paths, Path}
import java.io.{FileInputStream, File}
import org.apache.commons.codec.digest.DigestUtils

case class HashedFile(path: Path, hash: String) {
  def toProperty = hash + HashedFile.SEPARATOR + path
}

object HashedFile {
  val SEPARATOR = "="
  def fromProperty(s: String): HashedFile = {
    val index = s.indexOf(HashedFile.SEPARATOR)
    val hash = s.substring(0,index)
    val path = Paths.get(s.substring(index+1))
    HashedFile(path, hash)
  }

  def hash(f: File) = DigestUtils.md5Hex(new FileInputStream(f))
}