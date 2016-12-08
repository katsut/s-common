package commons.ddd.infrastructures.repositories

import java.sql.ResultSet

import commons.ddd.Domain

trait JdbcDxo[M, E] {
  def fromEntity(e: E)(implicit ctx: JdbcContext): M

  def toEntity(m: M): E
}

trait TreasureDataDxo[T <: Domain[_]] {
  def fromEntity(rs: ResultSet): T
}