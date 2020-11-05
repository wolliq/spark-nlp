package com.johnsnowlabs.nlp.annotators

import com.johnsnowlabs.nlp.AnnotatorApproach
import com.johnsnowlabs.nlp.AnnotatorType.DOCUMENT
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.Dataset


//SYNTAX: re.sub(pattern, repl, string[, count, flags])

/**
  * Tokenizes raw text from web scraping in document type columns into Sentence .
  *
  * This class represents a non fitted tokenizer. Fitting it will cause the internal RuleFactory to construct the rules for tokenizing from the input configuration.
  *
  * Identifies tokens with tokenization open standards. A few rules will help customizing it if defaults do not fit user needs.
  *
  * See [[https://github.com/JohnSnowLabs/spark-nlp/blob/master/src/test/scala/com/johnsnowlabs/nlp/annotators/TokenizerTestSpec.scala Tokenizer test class]] for examples examples of usage.
  *
  * @param uid required uid for storing annotator to disk
  * @groupname anno Annotator types
  * @groupdesc anno Required input and expected output annotator types
  * @groupname Ungrouped Members
  * @groupname param Parameters
  * @groupname setParam Parameter setters
  * @groupname getParam Parameter getters
  * @groupname Ungrouped Members
  * @groupprio param  1
  * @groupprio anno  2
  * @groupprio Ungrouped 3
  * @groupprio setParam  4
  * @groupprio getParam  5
  * @groupdesc Parameters A list of (hyper-)parameter keys this annotator can take. Users can set and get the parameter values through setters and getters, respectively.
  */
class SubStringReplacer(override val uid: String) extends AnnotatorApproach[SubStringReplacerModel] {
  /**
    *  Clears out rules and constructs a new rule for every combination of rules provided.
    *  The strategy is to catch one token per regex group.
    *  User may add its own groups if needs targets to be tokenized separately from the rest.
    *
    *  @param dataset
    *  @param recursivePipeline
    *  @return TokenizedModel
    *
    */
  override def train(dataset: Dataset[_], recursivePipeline: Option[PipelineModel]): SubStringReplacerModel = {
    new SubStringReplacerModel
  }

  override val description: String = "Annotator that identifies points of analysis in a useful manner"

  /** Input annotator type : DOCUMENT
    *
    * @group anno
    **/
  override val inputAnnotatorTypes: Array[AnnotatorType] = Array[AnnotatorType](DOCUMENT)

  /** Input annotator type : DOCUMENT
    *
    * @group anno
    **/
  override val outputAnnotatorType: AnnotatorType = DOCUMENT
}


