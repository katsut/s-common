package commons.ddd.infrastructures.repositories

import javax.sql.DataSource

import com.typesafe.config.Config
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import scalikejdbc.{ AutoSession, ConnectionPool, DBSession, DataSourceConnectionPool }
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import utils.DeferTracker

trait JdbcSupport {

  val conf: Config = ??? // TODO

  val _logger = LoggerFactory.getLogger(getClass)

  type RepositoryContextWithJdbc = RepositoryContext with JdbcContext { val jdbcConfig: DatabaseConfig[JdbcProfile] }

  def createJdbcContext()(implicit defer: DeferTracker): RepositoryContextWithJdbc = {

    val ctx = new RepositoryContext() with JdbcContext {
      override val jdbcConfig = DatabaseConfig.forConfig[JdbcProfile](path = "", config = conf.getConfig(s"slick.dbs.default"))
      override val session: DBSession = AutoSession
    }

    defer(ctx.jdbcConfig.db.close())

    ctx
  }

}

private object ScalikeJdbcSupport extends ScalikeJdbcSupport {

  def setUpDefault() {

    _logger.info("##### setup Default -- start")
    Class.forName("com.mysql.jdbc.Driver")

    // https://github.com/typesafehub/config
    val config = conf.getConfig("slick.dbs.default")
    //    val url = config.getString("db.url")
    //    val user = config.getString("db.user")
    //    val password = config.getString("db.password")
    //    ConnectionPool.add(ConnectionPool.DEFAULT_NAME ,url, user, password)

    val dataSource: DataSource = {
      val ds = new HikariDataSource()

      ds.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource")
      ds.addDataSourceProperty("url", config.getString("db.url"))
      ds.addDataSourceProperty("user", config.getString("db.user"))
      ds.addDataSourceProperty("password", config.getString("db.password"))

      ds.setMaximumPoolSize(config.getInt("maxConnections"))
      ds.setConnectionInitSql("select 1 from medias limit 1")
      ds
    }
    ConnectionPool.singleton(new DataSourceConnectionPool(dataSource))

    _logger.info("##### setup Default -- end")
  }

  def init() = ()

  setUpDefault()

}

trait ScalikeJdbcSupport extends JdbcSupport {

  ScalikeJdbcSupport.init()

  def createJdbcContext(_session: DBSession)(implicit defer: DeferTracker): RepositoryContextWithJdbc = {

    val ctx = new RepositoryContext() with JdbcContext {
      override val jdbcConfig = DatabaseConfig.forConfig[JdbcProfile](path = "", config = conf.getConfig(s"slick.dbs.default"))
      override val session: DBSession = _session
    }

    defer(ctx.jdbcConfig.db.close())

    defer(ctx.session.close())

    ctx
  }
}