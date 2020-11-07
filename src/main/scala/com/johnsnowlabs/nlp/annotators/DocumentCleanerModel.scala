package com.johnsnowlabs.nlp.annotators

import com.johnsnowlabs.nlp.AnnotatorType.DOCUMENT
import com.johnsnowlabs.nlp.{Annotation, AnnotatorModel, ParamsAndFeaturesReadable, ParamsAndFeaturesWritable}
import org.apache.spark.ml.param.Param
import org.apache.spark.ml.util.Identifiable


class DocumentCleanerModel(override val uid: String) extends AnnotatorModel[DocumentCleanerModel] {

  val EmptyStr = ""

  /** pattern to grab from text as token candidates. Defaults \\S+
    *
    * @group param
    **/
  val targetPattern: Param[String] = new Param(this, "targetPattern", "pattern to grab from text as token candidates. Defaults \\S+")

  /** removalPolicy to removal pattern from text
    *
    * @group param
    **/
  val removalPolicy: Param[String] = new Param(this, "removalPolicy", "removalPolicy to removal pattern from text")

  /** Annotator reference id. Used to identify elements in metadata or to refer to this annotator type */
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

  setDefault(
    targetPattern -> "<[^>]*>",
    removalPolicy -> "pretty_all"
  )

  /** pattern to grab from text as token candidates. Defaults <[^>]*>
    *
    * @group setParam
    **/
  def setTargetPattern(value: String): this.type = set(targetPattern, value)

  /** pattern to grab from text as token candidates. Defaults <[^>]*>
    *
    * @group getParam
    **/
  def getTargetPattern: String = $(targetPattern)

  /** pattern to grab from text as token candidates. Defaults pretty_all
    *
    * @group setParam
    **/
  def setRemovalPolicy(value: String): this.type = set(removalPolicy, value)

  /** pattern to grab from text as token candidates. Defaults pretty_all
    *
    * @group getParam
    **/
  def getRemovalPolicy: String = $(removalPolicy)

  private def withPrettyAllFormatter(s: String, pattern: String) = {
    val allReplacedStr = s.replaceAll(pattern, EmptyStr)
    allReplacedStr.split("\\s+").map(_.trim).mkString(" ")
  }

  private def withPrettyFirstFormatter(s: String, pattern: String) = {
    val firstReplacedStr = s.replaceFirst(pattern, EmptyStr)
    firstReplacedStr.split("\\s+").map(_.trim).mkString(" ")
  }

  private def searchAndRemove(s: String, pattern: String)(policy: String) = {
    require(!s.isEmpty && !pattern.isEmpty && !policy.isEmpty)

    policy match {
      case "all" => s.replaceAll(pattern, EmptyStr)
      case "pretty_all" => withPrettyAllFormatter(s, pattern)
      case "first" => s.replaceFirst(pattern, EmptyStr)
      case "pretty_first" => withPrettyFirstFormatter(s, pattern)
      case _ => throw new Exception("Wrong policy parameter in DocPatterRemover annotation")
    }
  }

  /**
    * takes a document and annotations and produces new annotations of this annotator's annotation type
    *
    * @param annotations Annotations that correspond to inputAnnotationCols generated by previous annotators if any
    * @return any number of annotations processed for every input annotation. Not necessary one to one relationship
    */
  override def annotate(annotations: Seq[Annotation]): Seq[Annotation] = {
    annotations.
      map(annotation =>
        Annotation(
          searchAndRemove(annotation.result, getTargetPattern)(getRemovalPolicy),
          annotation.metadata))
  }
}


object DocumentCleanerModel extends ParamsAndFeaturesReadable[NormalizerModel]