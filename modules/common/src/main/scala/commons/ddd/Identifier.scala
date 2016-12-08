package commons.ddd

trait Identifier[+A] {

  /**
   * 識別子の値。
   */
  def value: A

  val isDefined: Boolean = true

  val nonDefined: Boolean = !isDefined

  override def equals(obj: Any) = obj match {
    case that: Identifier[_] => value == that.value
    case that                => value == that
  }

  override lazy val hashCode = 31 * value.##

  override def toString = value.toString

}

object Identifier {
  def ofEmpty[ID >: Identifier[V], V]: Identifier[V] = EmptyIdentifier

}

object EmptyIdentifier extends Identifier[Nothing] {

  override val isDefined: Boolean = false

  override lazy val hashCode: Int = 0

  override val toString: String = s"${getClass.getSimpleName}(Nothing)"

  def value = sys.error("no such identifier")

}
