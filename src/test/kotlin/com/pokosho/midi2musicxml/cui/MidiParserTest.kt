package com.pokosho.midi2musicxml.cui

import com.pokosho.midi2musicxml.MidiParser
import com.pokosho.midi2musicxml.WarningType
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
    Assertions.assertEquals(parser.warnings.first().warningType, WarningType.NOTES_TOO_HIGH)
  }

  @Test
  fun testTooLow() {
    val parser = getParser("src/test/resources/test_too_low.mid", "おむれつ")
    Assertions.assertEquals(1, parser.warnings.size)
    Assertions.assertEquals(parser.warnings.first().warningType, WarningType.NOTES_TOO_LOW)
  }

  @Test
  fun testLyricIsTooShort() {
    val parser = getParser("src/test/resources/test_short.mid", "す")
    Assertions.assertEquals(1, parser.warnings.size)
    Assertions.assertEquals(parser.warnings.first().warningType, WarningType.LYRIC_COUNT_NOT_MATCH)
  }

  @Test
  fun testLyricIsTooLong() {
    val parser = getParser("src/test/resources/test_short.mid", "おむらいす")
    Assertions.assertEquals(1, parser.warnings.size)
    Assertions.assertEquals(parser.warnings.first().warningType, WarningType.LYRIC_COUNT_NOT_MATCH)
  }

  @Test
  fun testMultiTracks() {
    val parser = getParser("src/test/resources/test_multi_tracks.mid", "お")
    Assertions.assertEquals(1, parser.warnings.size)
    Assertions.assertEquals(parser.warnings.first().warningType, WarningType.MIDI_HASH_SOME_TRACKS)
  }

  @Test
  fun testSpecialChar1() {
    val parser = getParser("src/test/resources/test_short.mid", "ファイト")
    Assertions.assertEquals(3, parser.lyricWordCount())
    Assertions.assertEquals("ふぁ", parser.lyric.charAt(0).toString())
    Assertions.assertEquals("い", parser.lyric.charAt(1).toString())
  }
}
