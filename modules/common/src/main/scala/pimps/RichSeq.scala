package pimps

import scala.annotation.tailrec
import scala.collection.parallel.{ ForkJoinTaskSupport, ParSeq }
import scala.language.implicitConversions

object RichSeq {

  implicit class KeyBy[T](val s: Seq[T]) extends AnyVal {
    def keyBy[K](f: T => K): Map[K, T] = s.map(o => f(o) -> o).toMap
  }

  implicit class DistinctBy[T](val s: Seq[T]) extends AnyVal {
    /**
     * @param f 重複除去するキー
     * @return リスト内の重複キーを除去して返す
     */
    def distinctBy[K](f: T => K): Seq[T] = s.groupBy(f(_)).map(_._2.head).toSeq

    /**
     * @param f 重複除去するキー
     * @param ordering 重複除去する際の優先順
     * @return リスト内の重複キーを除去して返す
     */
    def distinctBy[K](f: T => K, ordering: (T, T) => Boolean): Seq[T] = s.groupBy(f(_)).map(_._2.sortWith(ordering).head).toSeq

    /**
     * @param f 重複除去するキー
     * @param ordering 重複除去する際の優先順
     * @return リスト内の重複キーを除去して返す
     */
    def distinctOrderingBy[K](f: T => K)(implicit ordering: Ordering[T]): Seq[T] = s.groupBy(f(_)).map(_._2.sorted.head).toSeq

    /**
     * @param take 取得件数
     * @param distinctKey 重複除去するキー
     * @param distinctLimit 重複を許容する上限数
     * @param ordering 重複除去する際の優先順
     * @return リストからtake件数を取得して返す。distinctKeyが同じものがリストに存在した場合、distinctLimitまで重複データを許容する
     */
    def takeWithDistinct[K](take: Int, distinctLimit: Int)(distinctKey: (T) => K)(implicit ordering: Ordering[T]): Seq[T] = {
      @tailrec
      def recursive(result: Seq[T], tails: Seq[T]): Seq[T] = tails.headOption match {
        case Some(t) if result.size >= take => result // 必要件数集まったら終了
        case Some(t) if result.count(distinctKey(_) == distinctKey(t)) < distinctLimit => recursive(result :+ t, tails.tail) // 重複キー上限未満なら追加して次の要素
        case Some(t) => recursive(result, tails.tail) // 重複キー上限に到達していたら追加せず次要素
        case None => result // リストが末尾まで到達したら終了
      }

      recursive(Seq.empty[T], s.sorted)
    }
  }

  implicit class ParWithTaskFactor[T](val s: Seq[T]) extends AnyVal {
    def parWithFactor(parallelismFactor: Int): ParSeq[T] = {
      val p = s.par
      p.tasksupport = new ForkJoinTaskSupport(new scala.concurrent.forkjoin.ForkJoinPool(parallelismFactor))
      p
    }

  }

}
