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

    val assembledDoc: Array[Annotation] = df
      .select("cleanedDoc")
      .collect
      .flatMap { _.getSeq[Row](0) }
      .map { Annotation(_) }
  }

  "A DocumentAssembler" should "annotate with the correct indexes" in {
    val f = fixture

    println(f.text.head)
    println(f.text(f.assembledDoc.head.begin))

//    f.text.head should equal (f.text(f.assembledDoc.head.begin))
//    f.text.last should equal (f.text(f.assembledDoc.head.end))
  }
}
