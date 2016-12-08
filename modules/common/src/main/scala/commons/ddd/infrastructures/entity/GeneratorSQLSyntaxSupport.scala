package commons.ddd.infrastructures.entity

import scalikejdbc.{ DBSession, SQLSyntaxSupport }

/**
 * Created by tsuruta on 2016/05/16.
 */
trait GeneratorSQLSyntaxSupport[EID, E] extends GeneratorSQLSyntaxSupportBase[E] {
  def find(id: EID)(implicit session: DBSession): Option[E]
}

trait GeneratorSQLSyntaxSupport2[EID1, EID2, E] extends GeneratorSQLSyntaxSupportBase[E] {
  def find(id: EID1, id2: EID2)(implicit session: DBSession): Option[E]
}

trait GeneratorSQLSyntaxSupportBase[E] extends SQLSyntaxSupport[E] {
  def findAll()(implicit session: DBSession): List[E]
  def countAll()(implicit session: DBSession): Long
  def save(entity: E)(implicit session: DBSession): E
  def destroy(entity: E)(implicit session: DBSession): Unit
}