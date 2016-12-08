package commons.ddd.infrastructures.repositories

import java.util.zip.GZIPInputStream

import awscala.s3.S3Client
import utils.DeferTracker

import scala.io.Source

/**
 * Created by tsuruta on 2016/01/28.
 */
trait S3Repository[T] extends Repository[T] with S3Dxo[T] {

  protected val s3: S3Client

  protected val bucket: String

  def scanLines(path: String)(f: (T) => Unit): Unit = DeferTracker.withDefer { defer =>
    val s3object = s3.getObject(bucket, path)
    defer(s3object.close())
    val stream = Source.fromInputStream(new GZIPInputStream(s3object.getObjectContent)) // GZip
    defer(stream.close())
    stream.getLines() flatMap fromEntity foreach f
  }

}
