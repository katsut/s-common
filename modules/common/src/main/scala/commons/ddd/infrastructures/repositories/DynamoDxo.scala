package commons.ddd.infrastructures.repositories

import com.amazonaws.services.dynamodbv2.document.Item

trait DynamoDxo[M] {
  def fromEntity(e: Item): M
  def toEntity(m: M): Item
}
