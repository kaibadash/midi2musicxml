package com.pokosho.midi2musicxml

fun String.kana2hiragana(): String {
  val sb = StringBuilder()
  this.chars().forEach {
    var c: Int = it
    if (c in 0x30A1..0x30F3) {
      c = (c - 0x0060)
    }
    sb.append(c.toChar())
  }
  return sb.toString()
}