package utils

import org.slf4j.LoggerFactory

import scala.util.{ Failure, Try }

/**
 * go言語でいう、defer 文法的な後処理機構 (Java でいう try-with-resources 的な機構) を提供します。
 *
 * スコープ終了時に登録された関数リストを実行することでリソースの解放漏れを防ぎます。
 *
 * 例)
 * ```
 * DeferUtils.withDefer { scopeFinalizer => // このスコープ終了時に登録関数が一括実行されます
 *   InputStream in = new FileInputStream(...)
 *   scopeFinalizer(IOUtils.closeQuietly(in)) // 解放関数の遅延実行 (defer) 登録
 *   ...
 *   val data = in.read()
 * }
 * ```
 */
object DeferTracker {
  def withDefer[A](context: DeferTracker => A): A = {
    val dt = new DeferTracker()
    try { context(dt) } finally { dt.makeCalls() }
  }
}

class DeferTracker() {

  val logger = LoggerFactory.getLogger(this.getClass)

  class LazyVal[A](val value: () => A)

  private var l: List[LazyVal[Any]] = List.empty[LazyVal[Any]]

  /**
   * (関数から抜ける等の) スコープ終了時に実行する関数を登録します。
   *
   * @param f スコープ終了時に遅延実行する関数 (関数の引数なし)。
   */
  def apply(f: => Any): Unit = {
    l = new LazyVal(() => f) :: l
  }

  /**
   * 登録されている遅延実行対象の関数を登録順に実行します。
   * @throws Throwable 関数で発生した例外の内、リストの先頭のもの。
   *                   最初のものしかスローされないため、関数内でもエラーログ出力をしてください。
   */
  def makeCalls(): Unit = {
    l.map(f => Try { f.value() }).filter(_.isFailure) match {
      case (head: Failure[_]) :: rest => throw head.exception
      case _                          =>
    }
  }

}
