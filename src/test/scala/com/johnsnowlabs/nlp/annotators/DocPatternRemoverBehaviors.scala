package com.johnsnowlabs.nlp.annotators

import com.johnsnowlabs.nlp.{Annotation, AnnotatorBuilder, ContentProvider, DataBuilder}
import org.apache.spark.sql.Row
import org.scalatest.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest._

import scala.language.reflectiveCalls

trait DocPatternRemoverBehaviors extends FlatSpec {

  def fixture = new {
    val text = ContentProvider.scrapedEnglishPhrase
    val df = AnnotatorBuilder.withDocPatterRemoverPipeline(DataBuilder.basicDataBuild(text))

    val cleanedDoc: Array[Annotation] = df
      .select("cleanedDoc")
      .collect
      .flatMap { _.getSeq[Row](0) }
      .map { Annotation(_) }
  }

  "A DocPatternRemover" should "annotate with the correct indexes" in {
    val f = fixture

    0 should equal (f.cleanedDoc.head.begin)
    59 should equal (f.cleanedDoc.head.`end`)
  }

  "A DocPatternRemover" should "annotate with the correct metadata" in {
    val f = fixture

    Map("sentence" -> "0") should equal (f.cleanedDoc.head.metadata)
  }
}
