package com.pokosho.midi2musicxml

import com.atilika.kuromoji.ipadic.Tokenizer
import com.google.common.io.ByteSource
import com.google.common.io.Files
import java.io.File
import java.io.IOException
import java.io.InputStream

class Lyric {
  private val warnings: MutableSet<String> = mutableSetOf()
  internal var lyric = ""

  constructor()

  constructor(pathToFile: String) {
    lyric = Files.readLines(File(pathToFile), Charsets.UTF_8).joinToString("")
  }

  constructor(stdin: InputStream) {
    val byteSource: ByteSource = object : ByteSource() {
      @Throws(IOException::class)
      override fun openStream(): InputStream {
        return stdin
      }
    }
    lyric = byteSource.asCharSource(Charsets.UTF_8).read()
  }

  fun setLyric(lyric: String) {
    this.lyric = lyric
  }

  fun warnings(): Array<String> {
    return warnings.toTypedArray()
  }

  private fun toHiragana(): Lyric {
    val tokenizer = Tokenizer.Builder().build()
    val tokens = tokenizer.tokenize(lyric)
    val regex = Regex("[0-9０-９a-zA-Zａ-ｚＡ-Ｚ]")
    this.lyric = tokens.map {
      if (arrayOf("*", "、", "。").contains(it.baseForm)) {
        if (regex.containsMatchIn(it.surface)) {
          this.warnings.add("English words are contained. They are ignored.")
        }
        ""
      } else {
        it.reading.kana2hiragana()
      }
    }.joinToString("")
    return this
  }

  private fun trim(): Lyric {
    this.lyric = lyric.trim()
    return this
  }

  override fun toString(): String {
    return trim().toHiragana().lyric
  }
}
