package pimps

import org.specs2.mutable.Specification

class RichSeqSpec extends Specification {

  import RichSeq._

  "RichSeq DistinctBy" in {

    "random pickup" in {
      val example: List[(Int, String)] = (1 -> "a") :: (2 -> "b") :: (3 -> "a") :: (4 -> "c") :: (1 -> "e") :: Nil

      // valueで重複除去
      val result = example.distinctBy(_._2)

      "value [a]" should {
        result count (_._2 == "a") must_== 1 // "a"は２件存在したが、１件に減っている
        result must haveSize(example.size - 1)
      }
    }

    "sorted pickup" in {
      val example: List[(Int, String)] = (1 -> "a") :: (2 -> "b") :: (3 -> "a") :: (4 -> "c") :: (1 -> "e") :: (3 -> "a") :: (5 -> "a") :: (2 -> "a") :: Nil

      // valueで重複除去
      val result = example.distinctBy(_._2, (l, r) => l._1 > r._1)

      "value [a]" should {
        // "a"は5件存在したが、１件に減り、_1が最大値のリストが残る
        result count (_._2 == "a") must_== 1
        result find (_._2 == "a") map (_._1) must_== Some(5)
      }
    }

    "sorted pickup with ordering" in {
      val example: List[(Int, String)] = (1 -> "a") :: (2 -> "b") :: (3 -> "a") :: (4 -> "c") :: (1 -> "e") :: (3 -> "a") :: (5 -> "a") :: (2 -> "a") :: Nil

      // valueで重複除去
      implicit val ordering = Ordering.by[(Int, String), Int](_._1).reverse // Keyの降順
      val result = example.distinctOrderingBy(_._2)

      "value [a]" should {
        // "a"は5件存在したが、１件に減り、_1が最大値のリストが残る
        result count (_._2 == "a") must_== 1
        result find (_._2 == "a") map (_._1) must_== Some(5)
      }
    }

    "sorted take with ordering" in {
      "Seq.len >= take" in {

        val example: List[(Int, String)] = (1 -> "a") :: (2 -> "b") :: (3 -> "a") :: (4 -> "c") :: (1 -> "e") :: (3 -> "a") :: (5 -> "a") :: (2 -> "a") :: (6 -> "c") :: (1 -> "d") :: Nil

        // valueで重複除去
        implicit val ordering = Ordering.by[(Int, String), Int](_._1).reverse // Keyの降順
        val result = example.takeWithDistinct(6, 2)(_._2) // ７件抽出、_._2が同じものは２件まで

        "value [a]" should {
          result count (_._2 == "a") must_== 2
          result.map(_._1) must_== Seq(6, 5, 4, 3, 2, 1) // IDの降順に並ぶ&"a"のデータは降順2件のみ
          // 6件取得できる
          result must haveSize(6)
        }
      }
      "Seq.len <= take" in {

        val example: List[(Int, String)] = (1 -> "a") :: (2 -> "b") :: (3 -> "a") :: (4 -> "c") :: (1 -> "e") :: (3 -> "a") :: (5 -> "a") :: (2 -> "a") :: (6 -> "c") :: (1 -> "d") :: Nil

        // valueで重複除去
        implicit val ordering = Ordering.by[(Int, String), Int](_._1).reverse // Keyの降順
        val result = example.takeWithDistinct(8, 2)(_._2) // ７件抽出、_._2が同じものは２件まで

        "value [a]" should {
          result count (_._2 == "a") must_== 2
          result.map(_._1) must_== Seq(6, 5, 4, 3, 2, 1, 1) // IDの降順に並ぶ&"a"のデータは降順2件のみ
          // 8件取得指定だが、aの重複を除去したので7件取得できる
          result must haveSize(7)
        }
      }
    }

  }

  "RichSeq KeyBy" in {

    val example = 1 to 5

    val result = example.keyBy(_ * 2)

    "value key" should {
      result foreach {
        case (l, r) =>
          l must_== (r * 2)
      }
      result must haveSize(example.size)

    }

  }

}
