package commons.ddd.infrastructures.repositories

import commons.ddd.{ Domain, Identifier }
import commons.ddd.infrastructures.entity.{ GeneratorSQLSyntaxSupport, GeneratorSQLSyntaxSupport2 }
import scalikejdbc._

import scala.language.reflectiveCalls

/**
 * 単項目キー用のリポジトリテンプレート。
 * 複合キー向けは、[[ScalikeJdbcRepository2]] を使用してください。
 *
 * @tparam DID ドメインモデルの ID 型。
 * @tparam D ドメインモデルの型。
 * @tparam EID エンティティの ID 型。
 * @tparam E エンティティの型。
 *
 */
trait ScalikeJdbcRepository[DID <: Identifier[EID], D <: Domain[DID], EID, E] extends Repository[D] with JdbcDxo[D, E] {

  def _create(entity: E)(implicit session: DBSession): E

  def create(domain: D)(implicit ctx: JdbcContext): D =
    fromEntity(_create(toEntity(domain))(ctx.session))

  protected val table: GeneratorSQLSyntaxSupport[EID, E]

  def resolveById(id: DID)(implicit ctx: JdbcContext): Option[D] =
    table.find(id.value)(ctx.session).map(fromEntity)

  def resolveAll()(implicit ctx: JdbcContext): Seq[D] =
    table.findAll()(ctx.session).map(fromEntity)

  def countAll()(implicit ctx: JdbcContext): Long =
    table.countAll()(ctx.session)

  def update(domain: D)(implicit ctx: JdbcContext): D =
    fromEntity(table.save(toEntity(domain))(ctx.session))

  def replace(domain: D)(implicit ctx: JdbcContext): D =
    resolveById(domain.id) match {
      case Some(_) => update(domain)
      case None    => create(domain)
    }

  def destroy(domain: D)(implicit ctx: JdbcContext): Unit =
    table.destroy(toEntity(domain))(ctx.session)

}

/**
 * 複合キー用のリポジトリテンプレート。
 *
 * 単項目キー向けは、[[ScalikeJdbcRepository]] を使用してください。
 *
 * @tparam DID ドメインモデルの複合 ID 型。
 * @tparam D ドメインモデルの型。
 * @tparam EID1 エンティティの 1 番目の ID 型。
 * @tparam EID2 エンティティの 2 番目の ID 型。
 * @tparam E エンティティの型。
 */
trait ScalikeJdbcRepository2[DID <: Identifier[_], D <: Domain[DID], EID1, EID2, E] extends Repository[D] with JdbcDxo[D, E] {

  def convertEntityIds(id: DID): (EID1, EID2)

  def _create(entity: E)(implicit session: DBSession): E

  def create(domain: D)(implicit ctx: JdbcContext): D =
    fromEntity(_create(toEntity(domain))(ctx.session))

  protected val table: GeneratorSQLSyntaxSupport2[EID1, EID2, E]

  def resolveById(id: DID)(implicit ctx: JdbcContext): Option[D] = {
    val eid = convertEntityIds(id)
    table.find(eid._1, eid._2)(ctx.session).map(fromEntity)
  }

  def resolveAll()(implicit ctx: JdbcContext): Seq[D] =
    table.findAll()(ctx.session).map(fromEntity)

  def countAll()(implicit ctx: JdbcContext): Long =
    table.countAll()(ctx.session)

  def update(domain: D)(implicit ctx: JdbcContext): D =
    fromEntity(table.save(toEntity(domain))(ctx.session))

  def replace(domain: D)(implicit ctx: JdbcContext): D =
    resolveById(domain.id) match {
      case Some(_) => update(domain)
      case None    => create(domain)
    }

  def destroy(domain: D)(implicit ctx: JdbcContext): Unit =
    table.destroy(toEntity(domain))(ctx.session)

}
