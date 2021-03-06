package uima.ae.ja

import java.util.Locale

import ir.fulltext.indri.ja.{JapaneseRetrievalByBoW, JapaneseRetrievalByKeyword}
import jeqa.types.{BoWQuery, Geography, KeywordQuery}
import org.apache.uima.jcas.JCas
import text.correction.ja.{JapaneseBoWBasedIRDocCorrector, JapaneseKeywordBasedIRDocCorrector}
import uima.ae.MultiLingualInformationRetriever

import scala.collection.mutable

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseInformationRetriever extends MultiLingualInformationRetriever with JapaneseDocumentAnnotator {
  override protected def retrieveByKeyword(aJCas: JCas,
                                  query: KeywordQuery,
                                  keywordCorrectionMap: mutable.Map[String, Seq[String]]): Option[Long] = {
    if (localeId != Locale.JAPANESE.getLanguage) {
      return None
    }
    Option(
      JapaneseRetrievalByKeyword.retrieve(
        aJCas,
        query,
        keywordCorrectionMap,
        mIndriScoreIndex,
        mDocumentId
      )
    )
  }

  override protected def retrieveByBoW(aJCas: JCas, query: BoWQuery): Option[Long] = {
    if (localeId != Locale.JAPANESE.getLanguage) {
      return None
    }
    Option(
      JapaneseRetrievalByBoW.retrieve(
        aJCas,
        query,
        mIndriScoreIndex,
        mDocumentId
      )
    )
  }

  override protected def correctDocByKeyword(aJCas: JCas,
                                    query: KeywordQuery,
                                    keywordCorrectionMap: Map[String, Seq[String]],
                                    beginTimeLimit: Option[Int],
                                    endTimeLimit: Option[Int],
                                    geographyLimit: Option[Geography]): Unit = {
    JapaneseKeywordBasedIRDocCorrector.correct(
      aJCas,
      query,
      keywordCorrectionMap,
      beginTimeLimit,
      endTimeLimit,
      geographyLimit
    )
  }

  override protected def correctDocByBoW(aJCas: JCas,
                                query: BoWQuery,
                                beginTimeLimit: Option[Int],
                                endTimeLimit: Option[Int],
                                geographyLimit: Option[Geography]): Unit = {
    JapaneseBoWBasedIRDocCorrector.correct(
      aJCas,
      query,
      beginTimeLimit,
      endTimeLimit,
      geographyLimit
    )
  }
}
