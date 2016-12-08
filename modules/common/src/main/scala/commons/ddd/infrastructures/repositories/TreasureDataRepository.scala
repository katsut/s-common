package commons.ddd.infrastructures.repositories

import commons.ddd.Domain
import org.joda.time.DateTime
import utils.DeferTracker

abstract class TreasureDataRepository[D <: Domain[_]] extends Repository[D] with TreasureDataDxo[D] {
  def resolveByTimeRange(from: Option[DateTime], to: Option[DateTime])(implicit defer: DeferTracker): Iterator[D]
}
