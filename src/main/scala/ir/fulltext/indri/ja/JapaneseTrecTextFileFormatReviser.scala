package ir.fulltext.indri.ja

import converter.MultiLingualNgramSegmentator
import converter.ja.JapaneseNgramSegmentator
import ir.fulltext.indri.MultiLingualTrecTextFileFormatReviser
import m17n.Japanese
import sentence.ja.JapaneseSentenceSplitter
import text.StringOption
import text.analyzer.mor.mecab.UnidicMecab

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
class JapaneseTrecTextFileFormatReviser(nGram: Int, isChar: Boolean)
  extends MultiLingualTrecTextFileFormatReviser(nGram, isChar)
    with Japanese {
  override protected def normalizeSentences(line: String): String = {
    val builder = new StringBuilder()
    JapaneseSentenceSplitter.split(StringOption(line)) foreach {
      sentence =>
        builder.append(sentence.text)
    }
    builder.result
  }

  override protected val segmentator: MultiLingualNgramSegmentator = {
    new JapaneseNgramSegmentator(nGram)
  }

  override protected def segment(text: StringOption, isContentWord: Boolean): String = {
    if (isContentWord) {
      segmentator.segmentateWithToken(UnidicMecab.extractWords(text)).getOrElse("")
    } else {
      segmentator.asInstanceOf[JapaneseNgramSegmentator].segmentateWithCharacter(text).getOrElse("")
    }
  }
}
