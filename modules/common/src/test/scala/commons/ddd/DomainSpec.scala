package commons.ddd

import org.specs2.mutable.Specification

class DomainSpec extends Specification {

  private case class SampleDomain(
    id: SampleDomainId,
    msg: String
  ) extends Domain[SampleDomainId]

  case class SampleDomainId(value: Int) extends Identifier[Int]

  "SampleDomain" should {
    "apply" in {
      val domain = new SampleDomain(SampleDomainId(1), "hoge")
      "identifier hashcode" in {
        domain.id must_== 1
        domain.id must_== SampleDomainId(1)
      }

      domain.msg must_== "hoge"
    }
  }
}
