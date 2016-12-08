package utils

import org.specs2.mutable.Specification

class DeferTrackerSpec extends Specification {

  "Defer" should {
    "defer使用した場合、ブロック終了後にインクリメントされる" in {
      var a = 0
      DeferTracker.withDefer { defer =>
        defer(a = a + 1)
        "call defer block" in {
          a must_== 0
        }
      }
      a must_== 1
    }

    "defer未使用" in {
      var a = 0
      a = a + 1
      a must_== 1
    }
  }
}