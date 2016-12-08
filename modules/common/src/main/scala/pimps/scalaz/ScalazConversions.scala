package pimps.scalaz

import scala.language.implicitConversions
import scalaz.Monoid

/**
 * Created by tsuruta on 2016/02/12.
 */
sealed trait ScalazConversions {
  implicit object MonoidDouble extends Monoid[Double] {
    override def zero: Double = 0.0
    override def append(f1: Double, f2: => Double): Double = f1 + f2
  }
}

object ScalazConversions extends ScalazConversions
