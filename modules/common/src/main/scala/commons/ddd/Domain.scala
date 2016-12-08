package commons.ddd

trait Domain[ID <: Identifier[_]] {

  val id: ID

  override def equals(obj: Any): Boolean = this match {
    case that: Domain[_] => id == that.id
    case _               => false
  }

  override def hashCode: Int = 31 * id.##

  def isCreated = true // TODO

}
