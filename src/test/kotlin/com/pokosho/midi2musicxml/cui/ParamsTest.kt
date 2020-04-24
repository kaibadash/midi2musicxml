package com.pokosho.midi2musicxml.cui

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

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
    // Assertions.assertEquals(params.lyric!!.toString(), "")
    Assertions.assertEquals(params.lyricFile , "")
    Assertions.assertEquals(params.outputPath, command + ".musicxml")
    Assertions.assertTrue(params.warnings.isEmpty())
  }

  @Test
  fun testWithLyric() {
    val command = "$midi -t src/test/resources/test_short.txt"
    val params = Params(command.split(" ").toTypedArray(), System.`in`)
    // Assertions.assertEquals(params.lyric!!.toString(), "うい")
  }

  @Test
  fun testWithCallNeutrino() {
    val command = "$midi -n"
    val params = Params(command.split(" ").toTypedArray(), System.`in`)
    Assertions.assertTrue(params.callNeutrino)
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
