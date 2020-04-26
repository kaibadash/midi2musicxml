package com.pokosho.midi2musicxml.cui

import com.pokosho.midi2musicxml.MidiParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MidiParserTest {
  private fun getParser(midi: String, lyric: String): MidiParser {
    val parser = MidiParser()
    parser.parse(midi, lyric)
    parser.generateXML(midi + ".musicxml")
    return parser
  }

  @Test
  fun testValidMidi() {
    val parser = getParser("src/test/resources/test_short.mid", "すし")
    Assertions.assertEquals(0, parser.warnings.size)
  }

  @Test
  fun testTooHigh() {
    val parser = getParser("src/test/resources/test_too_high.mid", "おむれつ")
    Assertions.assertEquals(1, parser.warnings.size)
    Assertions.assertTrue(parser.warnings.first().contains("high"))
  }

  @Test
  fun testTooLow() {
    val parser = getParser("src/test/resources/test_too_low.mid", "おむれつ")
    Assertions.assertEquals(1, parser.warnings.size)
    Assertions.assertTrue(parser.warnings.first().contains("low"))
  }

  @Test
  fun testLyricIsTooShort() {
    val parser = getParser("src/test/resources/test_short.mid", "す")
    Assertions.assertEquals(1, parser.warnings.size)
    Assertions.assertTrue(parser.warnings.first().contains("lyric"))
  }

  @Test
  fun testLyricIsTooLong() {
    val parser = getParser("src/test/resources/test_short.mid", "おむらいす")
    Assertions.assertEquals(1, parser.warnings.size)
    Assertions.assertTrue(parser.warnings.first().contains("lyric"))
  }

  @Test
  fun testMultiTracks() {
    val parser = getParser("src/test/resources/test_multi_tracks.mid", "お")
    Assertions.assertEquals(1, parser.warnings.size)
    Assertions.assertTrue(parser.warnings.first().contains("track"))
  }


//  MIDI has multi tracks
//  A lyric contains English words
}
