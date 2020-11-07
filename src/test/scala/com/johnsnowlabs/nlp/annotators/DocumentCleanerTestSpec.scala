package com.johnsnowlabs.nlp.annotators

import com.johnsnowlabs.nlp.AnnotatorType
import org.scalatest.FlatSpec

class DocumentCleanerTestSpec extends FlatSpec with DocumentCleanerBehaviors {
  val documentCleaner = new DocumentCleaner()

  "a DocumentCleaner" should s"be of type ${AnnotatorType.DOCUMENT}" in {
    assert(documentCleaner.outputAnnotatorType == AnnotatorType.DOCUMENT)
  }
}
