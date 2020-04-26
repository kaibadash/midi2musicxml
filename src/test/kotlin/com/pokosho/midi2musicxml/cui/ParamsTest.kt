package com.pokosho.midi2musicxml.cui

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ParamsTest {
  val midi = "src/test/resources/test_short.mid"

  @Test
  fun testNoParams() {
    val command = ""
    assertThrows<IllegalArgumentException> {
      Params(command.split(" ").toTypedArray(), System.`in`)
    }
  }

  @Test
  fun testShowHelp() {
    val command = "--help"
    val params = Params(command.split(" ").toTypedArray(), System.`in`)
    Assertions.assertTrue(params.showHelp)
  }

  @Test
  fun testWithMid() {
    val command = midi
    val params = Params(command.split(" ").toTypedArray(), System.`in`)
    Assertions.assertFalse(params.showHelp)
    Assertions.assertFalse(params.silent)
    Assertions.assertEquals(params.lyric.toString(), "")
    Assertions.assertEquals(params.lyricFile, "")
    Assertions.assertEquals(params.outputPath, "src/test/resources/test_short.musicxml")
  }

  @Test
  fun testWithLyric() {
    val command = "$midi -t src/test/resources/test_short.txt"
    val params = Params(command.split(" ").toTypedArray(), System.`in`)
    Assertions.assertEquals("うい", params.lyric.toString())
  }

  @Test
  fun testWithCallNeutrino() {
    val dir = "src/test/resources/NEUTRINO"
    val command = "$midi -n $dir"
    val params = Params(command.split(" ").toTypedArray(), System.`in`)
    Assertions.assertEquals(dir, params.neutrinoDir)
  }

  @Test
  fun testWithSilent() {
    val command = "$midi -s"
    val params = Params(command.split(" ").toTypedArray(), System.`in`)
    Assertions.assertTrue(params.silent)
  }

  @Test
  fun testWithOutput() {
    val output = "/tmp/test.mid.musicxml"
    val command = "$midi -o $output"
    val params = Params(command.split(" ").toTypedArray(), System.`in`)
    Assertions.assertEquals(params.outputPath, output)
  }
}
