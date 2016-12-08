package utils

import com.github.nscala_time.time.Imports._

object DateTimeConstants extends DateTimeConstants

trait DateTimeConstants {

  val TIME_ZONE_JST = DateTimeZone.forID("Asia/Tokyo")

}
