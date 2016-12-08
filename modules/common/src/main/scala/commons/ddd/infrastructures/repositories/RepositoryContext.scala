package commons.ddd.infrastructures.repositories

import scalikejdbc.DBSession
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

class RepositoryContext() {

}

trait JdbcContext {
  self: RepositoryContext =>

  val jdbcConfig: DatabaseConfig[JdbcProfile]

  val session: DBSession

}
