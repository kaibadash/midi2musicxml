package com.pokosho.midi2musicxml

import com.atilika.kuromoji.ipadic.Tokenizer
import com.google.common.io.ByteSource
import com.google.common.io.Files
import com.pokosho.midi2musicxml.extension.kana2hiragana
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class Lyric {
  private val warnings: MutableSet<String> = mutableSetOf()
  private val lyric = mutableListOf<PronouncedWord>()
  private var originalLyric: String = ""

  constructor()

  constructor(pathToFile: String) {
    parse(toHiragana(Files.readLines(File(pathToFile), Charsets.UTF_8).joinToString("\n")))
  }

  constructor(stdin: InputStream) {
    val byteSource: ByteSource = object : ByteSource() {
      @Throws(IOException::class)
      override fun openStream(): InputStream {
        return stdin
      }
    }
    parse(toHiragana(byteSource.asCharSource(Charsets.UTF_8).read()))
  }

  companion object {
    fun fromString(string: String): Lyric {
      return Lyric(string.byteInputStream(Charset.defaultCharset()))
    }
  }

  fun warnings(): Array<String> {
    return warnings.toTypedArray()
  }

  private fun parse(string: String) {
    this.originalLyric = string
    this.lyric.addAll(PronouncedWord.toPronouncedWords(string))
  }

  private fun toHiragana(string: String): String {
    val tokenizer = Tokenizer.Builder().build()
    val tokens = tokenizer.tokenize(string.trim())
    val regex = Regex("[0-9０-９a-zA-Zａ-ｚＡ-Ｚ]")
    val regexReadable = Regex("^[あ-んア-ン]+$")

    val readings = tokens.map {
      if (it.surface == "\n") {
        return@map it.surface
      }
      if (regexReadable.matches(it.surface)) {
        return@map it.surface
      }
      if (arrayOf("*", "、", "。").contains(it.baseForm)) {
        if (regex.containsMatchIn(it.surface)) {
          this.warnings.add("English or numbers words are contained. They are ignored.")
        }
        ""
      } else {
        it.pronunciation
      }
    }
    return removeUnpronounced(readings.joinToString("").kana2hiragana())
  }

  fun count(): Int {
    return lyric.filter { it.toString().isNotBlank() }.count()
  }

  fun charAt(index: Int): PronouncedWord {
    if (index > lyric.count() - 1) {
      return PronouncedWord("")
    }
    return this.lyric[index]
  }

  private fun removeUnpronounced(string: String): String {
    return string.replace("[〜ーっッ]".toRegex(), "")
  }

  /**
   * Get lyric with line break for preview.
   */
  fun stringForPreview(): String {
    return PronouncedWord.toPronouncedWords(this.originalLyric, true).map {
      if (it.toString() == "\n") {
        it
      } else {
        it.toString()
      }
    }.joinToString("")
  }

  override fun toString(): String {
    return lyric.joinToString("")
  }
}
