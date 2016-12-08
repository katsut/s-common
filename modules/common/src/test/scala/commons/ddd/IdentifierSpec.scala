package commons.ddd

import org.specs2.mutable.Specification

class IdentifierSpec extends Specification {

  "Identifier" should {
    "some id" in {
      "AnyVal identifier value" in {
        case class SampleDomainId(value: Int) extends Identifier[Int]
        val id = SampleDomainId(1)
        "identifier toString returns value toString()" in {
          id.value.toString must_== id.toString
        }
        "isDefined is true" in {
          id.isDefined must beTrue
        }
        "nonDefined is false" in {
          id.nonDefined must beFalse
          List.empty
        }
      }
      "AnyRef identifier value" in {
        case class SampleDomainId(value: (Int, String)) extends Identifier[(Int, String)]
        val id = SampleDomainId(1 -> "hage")
        "identifier toString returns value toString()" in {
          id.value.toString must_== id.toString
        }
        "isDefined is true" in {
          id.isDefined must beTrue
        }
        "nonDefined is false" in {
          id.nonDefined must beFalse
        }
      }
    }
    //    "empty id" in {
    //      "AnyVal identifier value" in {
    //        case class SampleDomainId(value: Int) extends Identifier[Int]
    //        val id: SampleDomainId = // Identifier.ofEmpty[Identifier[Int], Int] FIXME ofEmpty実装
    //          "isDefined is false" in {
    //            id.isDefined must beFalse
    //          }
    //        "nonDefined is true" in {
    //          id.nonDefined must beTrue
    //        }
    //      }
    //    }
  }
}
