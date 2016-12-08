package commons.ddd.infrastructures.repositories

trait S3Dxo[M] {
  def fromEntity(e: String): Option[M]
  def toEntity(m: M): String
}
