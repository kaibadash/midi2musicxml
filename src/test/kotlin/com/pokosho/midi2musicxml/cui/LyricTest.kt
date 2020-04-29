package com.pokosho.midi2musicxml.cui

import com.pokosho.midi2musicxml.Lyric
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.FileInputStream

class LyricTest {
  @Test
  fun testWithFile() {
    Assertions.assertEquals("うい", Lyric("src/test/resources/test_short.txt").toString())
  }

  @Test
  fun testWithInputStream() {
    Assertions.assertEquals("うい", Lyric(FileInputStream("src/test/resources/test_short.txt")).toString())
  }

  @Test
  fun testWithHiragana() {
    val lyric = Lyric.fromString("うい")
    Assertions.assertEquals("うい", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 0)
  }

  @Test
  fun testWithKanji() {
    val lyric = Lyric.fromString("魑魅魍魎")
    Assertions.assertEquals("ちみもりょ", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 0)
  }

  @Test
  fun testWithSymbol() {
    val lyric = Lyric.fromString("魑魅魍魎!?^^;")
    Assertions.assertEquals("ちみもりょ", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 0)
  }

  @Test
  fun testWithEnglish() {
    val lyric = Lyric.fromString("Helloどうも俺はここ")
    Assertions.assertEquals("どもおれわここ", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 1)
  }

  @Test
  fun testWithMultiByteEnglish() {
    val lyric = Lyric.fromString("ＨＥＬＬＯどうも俺はここ")
    Assertions.assertEquals("どもおれわここ", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 1)
  }

  @Test
  fun testWithPeriod() {
    val lyric = Lyric.fromString("寿司、酒。刺し身,肉.")
    Assertions.assertEquals("すしさけさしみにく", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 0)
  }

  @Test
  fun testWithLF() {
    val lyric = Lyric.fromString("""
餃子
しゃぶしゃぶ
焼肉
ビールで流し込みたい
刺し身
鍋
日本酒で流し込みたい""")
    Assertions.assertEquals("ぎょざしゃぶしゃぶやきにくびるでながしこみたいさしみなべにぽんしゅでながしこみたい", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 0)
  }

  @Test
  fun testUnpronouncedWord() {
    val lyric = Lyric.fromString("ヨーロッパ")
    Assertions.assertEquals("よろぱ", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 0)
  }

  @Test
  fun testReplaceForPronunciation() {
    val lyric = Lyric.fromString("私は鳥")
    Assertions.assertEquals("わたしわとり", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 0)
  }
}
