package com.pokosho.midi2musicxml.cui

import com.pokosho.midi2musicxml.PronouncedWord
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PronouncedWordTest {
  @Test
  fun testUnpronouncedWord() {
    val list = PronouncedWord.toPronouncedWords("ふぁいとフェ")
    Assertions.assertEquals("ふぁ", list[0].toString())
    Assertions.assertEquals("い", list[1].toString())
    Assertions.assertEquals("と", list[2].toString())
    Assertions.assertEquals("フェ", list[3].toString())
  }
}