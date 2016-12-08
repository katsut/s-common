package commons.ddd.infrastructures.repositories

/**
 * Created by tsuruta on 2016/01/28.
 */
trait DynamoRepository[T] extends Repository[T] {
  def resolveById(primaryKey: Any, rangeKey: Option[Any])(implicit ctx: RepositoryContext): Option[T]

  def put(domain: T): Unit
}
