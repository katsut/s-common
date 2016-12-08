package commons.ddd.infrastructures.repositories

import commons.ddd.{ Domain, Identifier }

abstract class SlickJdbcRepository[ID <: Identifier[_], D <: Domain[ID], E] extends Repository[D] with JdbcDxo[D, E] {

  def resolveById(id: ID)(implicit ctx: JdbcContext): Option[D]

  def resolveAll()(implicit ctx: JdbcContext): Seq[D]

  def create(domain: D)(implicit ctx: JdbcContext): Int

  def update(domain: D)(implicit ctx: JdbcContext): Int

}
