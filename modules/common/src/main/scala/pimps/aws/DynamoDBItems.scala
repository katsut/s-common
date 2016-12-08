package pimps.aws

import com.amazonaws.services.dynamodbv2.document.Item

object DynamoDBItems {

  implicit class WithOption(val item: Item) extends AnyVal {
    def withOption(name: String, value: Option[Any]): Item = {
      value match {
        case Some(s) => item.`with`(name, s)
        case None    => item.withNull(name)
      }

      item
    }
  }

}
