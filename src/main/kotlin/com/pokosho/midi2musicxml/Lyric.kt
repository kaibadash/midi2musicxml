package com.pokosho.midi2musicxml

import com.google.common.io.ByteSource
import com.google.common.io.Files
import java.io.File
import java.io.IOException
import java.io.InputStream

class Lyric {
  var lyric = ""

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

  fun warnings(): Array<String> {
    return arrayOf()
  }

  private fun toHiragana(): String {
    return lyric
  }

  override fun toString(): String {
    return toHiragana()
  }
}
