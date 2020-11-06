package com.johnsnowlabs.nlp.annotators

import com.johnsnowlabs.nlp.AnnotatorType
import org.scalatest.FlatSpec

class DocPatternRemoverTestSpec extends FlatSpec with DocPatternRemoverBehaviors {
  val docPattRemover = new DocPatternRemover()

  "a DocPatternRemover" should s"be of type ${AnnotatorType.DOCUMENT}" in {
    assert(docPattRemover.outputAnnotatorType == AnnotatorType.DOCUMENT)
  }
}
