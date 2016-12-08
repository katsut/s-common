package pimps

import scala.language.implicitConversions

object RichStrings {

  implicit class Opt(val s: String) extends AnyVal {
    def toOption: Option[String] = Option(s).filter(_.trim.nonEmpty)
  }

  implicit class MD5(val text: String) extends AnyVal {
    def toMD5: String = java.security.MessageDigest.getInstance("MD5").digest(text.getBytes).map("%02x".format(_)).mkString
  }

}

