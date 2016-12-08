package pimps

import org.joda.time.DateTime
import org.joda.time.DateTimeZone.UTC
import org.specs2.mutable.Specification

class RichDateTimeSpec extends Specification {

  import RichDateTime._

  "Zone JST" in {

    "utc -> jst" in {
      val utcDT = DateTime.now(UTC)
      val result = utcDT.withZoneJST

      result.getZone.getID must_== "Asia/Tokyo"
    }

  }

}
