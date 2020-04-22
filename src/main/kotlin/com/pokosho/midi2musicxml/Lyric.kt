package com.pokosho.midi2musicxml

import java.io.InputStream

class Lyric {
  constructor(pathToFile: String) {

  }

  constructor(stdin: InputStream) {

  }

  fun warnings(): List<String> {
    return listOf<String>("")
  }

  private fun toHiragana(): String {
    return ""
  }

  override fun toString(): String {
    return ""
  }
}
