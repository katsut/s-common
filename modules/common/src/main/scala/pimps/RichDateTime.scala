package pimps

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import utils.DateTimeConstants

object RichDateTime extends DateTimeConstants {

  val yyyy_MM_dd_HH_mm_ss = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

  implicit class ZoneJST(val dt: DateTime) extends AnyVal {
    def withZoneJST = dt withZone TIME_ZONE_JST
  }

}
