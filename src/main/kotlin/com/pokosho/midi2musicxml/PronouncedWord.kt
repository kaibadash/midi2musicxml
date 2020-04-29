package com.pokosho.midi2musicxml

/**
 * 発音する歌詞1文字を表現する。
 * 例えば「ふぁ」は2文字だが歌詞では1つとして扱う。
 */
class PronouncedWord(val word: String) {
  companion object {
    fun toPronouncedWords(string: String, withLineBreak: Boolean = false): List<PronouncedWord> {
      val splitter = if (withLineBreak) "splitter-${Math.random()}" else "\n"
      return string.replace("(.[ぁぃぅぇぉゃゅょァィゥェォャュョ]?)".toRegex(), "$1${splitter}")
        .trim().split(splitter).map {
          PronouncedWord(it)
        }
    }
  }

  override fun toString(): String {
    return word
  }
}