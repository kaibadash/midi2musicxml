package com.pokosho.midi2musicxml

import com.atilika.kuromoji.ipadic.Tokenizer
import com.google.common.io.ByteSource
import com.google.common.io.Files
import java.io.File
import java.io.IOException
import java.io.InputStream

class Lyric {
  private val warnings: MutableSet<String> = mutableSetOf()
  private var lyric: String = ""

  constructor()

  constructor(pathToFile: String) {
    this.lyric = toHiragaana(Files.readLines(File(pathToFile), Charsets.UTF_8).joinToString(""))
  }

  constructor(stdin: InputStream) {
    val byteSource: ByteSource = object : ByteSource() {
      @Throws(IOException::class)
      override fun openStream(): InputStream {
        return stdin
      }
    }
    this.lyric = toHiragaana(byteSource.asCharSource(Charsets.UTF_8).read())
  }

  fun setLiric(lyric: String) {
    this.lyric = toHiragaana(lyric)
  }

  fun warnings(): Array<String> {
    return warnings.toTypedArray()
  }

  private fun toHiragaana(string: String): String {
    val tokenizer = Tokenizer.Builder().build()
    val tokens = tokenizer.tokenize(string.trim())
    val regex = Regex("[0-9０-９a-zA-Zａ-ｚＡ-Ｚ]")

    val readings = tokens.map {
      if (arrayOf("*", "、", "。").contains(it.baseForm)) {
        if (regex.containsMatchIn(it.surface)) {
          this.warnings.add("English words are contained. They are ignored.")
        }
        ""
      } else {
        it.reading
      }
    }
    return readings.joinToString("").kana2hiragana()
  }

  override fun toString(): String {
    return lyric
  }
}
