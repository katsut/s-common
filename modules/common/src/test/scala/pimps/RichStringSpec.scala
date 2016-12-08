package pimps

import org.specs2.mutable.Specification

class RichStringSpec extends Specification {

  import RichStrings._

  "convert Option" in {

    "empty to None" in {
      "".toOption must_=== None
    }

    "null to None" in {
      val target: String = null
      target.toOption must_=== None
    }

    "string to Some(string)" in {
      "hage".toOption must_== Some("hage")
    }

  }

  "convert md5" in {
    "hage md5 ea703e7aa1efda0064eaa507d9e8ab7e" in {
      "hoge".toMD5 must_=== "ea703e7aa1efda0064eaa507d9e8ab7e"
    }

  }
}
