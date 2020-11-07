package com.johnsnowlabs.nlp.annotators

import com.johnsnowlabs.nlp.AnnotatorApproach
import com.johnsnowlabs.nlp.AnnotatorType.DOCUMENT
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.param.Param
import org.apache.spark.ml.util.{DefaultParamsReadable, Identifiable}
import org.apache.spark.sql.Dataset


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
class DocumentCleaner(override val uid: String) extends AnnotatorApproach[DocumentCleanerModel] {

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

  def this() = this(Identifiable.randomUID("DOC_PATTERN_REPLACER"))

  /** Pattern to grab from text as token candidates. Defaults \\S+
    *
    * @group Parameters
    **/
  val targetPattern: Param[String] = new Param(this, "targetPattern", "Pattern to replace from text")

  /** removalPolicy to removal pattern from text
    *
    * @group param
    **/
  val removalPolicy: Param[String] = new Param(this, "removalPolicy", "removalPolicy to removal pattern from text")

  /**
    * Set a basic regex rule to identify token candidates in text.
    *
    * Defaults to: "\\S+" which means anything not a space will be matched and considered as a token candidate, This will cause text to be split on on white spaces  to yield token candidates.
    *
    * This rule will be added to the BREAK_PATTERN variable, which is used to yield token candidates.
    *
    * {{{
    * import org.apache.spark.ml.Pipeline
    * import com.johnsnowlabs.nlp.annotators.Tokenizer
    * import com.johnsnowlabs.nlp.DocumentAssembler
    *
    * val textDf = sqlContext.sparkContext.parallelize(Array("I only consider lowercase characters and NOT UPPERCASED and only the numbers 0,1, to 7 as tokens but not 8 or 9")).toDF("text")
    * val documentAssembler = new DocumentAssembler().setInputCol("text").setOutputCol("sentences")
    * val tokenizer = new Tokenizer().setInputCols("sentences").setOutputCol("tokens").setTargetPattern("a-z-0-7")
    * new Pipeline().setStages(Array(documentAssembler, tokenizer)).fit(textDf).transform(textDf).select("tokens.result").show(false)
    * }}}
    *
    * This will yield : [only, consider, lowercase, characters, and, and, only, the, numbers, 0, 1, to, 7, as, tokens, but, not, or]
    *
    * @group setParam
    */
  def setTargetPattern(value: String): this.type = set(targetPattern, value)

  /** Basic regex rule to identify a candidate for tokenization. Defaults to \\S+ which means anything not a space
    *
    * @group getParam
    **/
  def getTargetPattern: String = $(targetPattern)

  /** pattern to grab from text as token candidates. Defaults \\S+
    *
    * @group setParam
    **/
  def setRemovalPolicy(value: String): this.type = set(removalPolicy, value)

  /** pattern to grab from text as token candidates. Defaults \\S+
    *
    * @group getParam
    **/
  def getRemovalPolicy: String = $(removalPolicy)

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
  override def train(dataset: Dataset[_], recursivePipeline: Option[PipelineModel]): DocumentCleanerModel = {
    new DocumentCleanerModel()
      .setTargetPattern($(targetPattern))
      .setRemovalPolicy($(removalPolicy))
  }
}


object DocumentCleaner extends DefaultParamsReadable[DocumentCleaner]