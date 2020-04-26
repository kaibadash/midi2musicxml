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
    val lyric = Lyric()
    lyric.setLyric("うい")
    Assertions.assertEquals("うい", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 0)
  }

  @Test
  fun testWithKanji() {
    val lyric = Lyric()
    lyric.setLyric("魑魅魍魎")
    Assertions.assertEquals("ちみもうりょう", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 0)
  }

  @Test
  fun testWithSymbol() {
    val lyric = Lyric()
    lyric.setLyric("魑魅魍魎!?^^;")
    Assertions.assertEquals("ちみもうりょう", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 0)
  }

  @Test
  fun testWithEnglish() {
    val lyric = Lyric()
    lyric.setLyric("Helloどうも俺はここ")
    Assertions.assertEquals("どうもおれはここ", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 1)
  }

  @Test
  fun testWithMultiByteEnglish() {
    val lyric = Lyric()
    lyric.setLyric("ＨＥＬＬＯどうも俺はここ")
    Assertions.assertEquals("どうもおれはここ", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 1)
  }

  @Test
  fun testWithPeriod() {
    val lyric = Lyric()
    lyric.setLyric("寿司、酒。刺し身,肉.")
    Assertions.assertEquals("すしさけさしみにく", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 0)
  }

  @Test
  fun testWithLF() {
    val lyric = Lyric()
    lyric.setLyric("""
餃子
しゃぶしゃぶ
焼肉
ビールで流し込みたい
刺し身
鍋
日本酒で流し込みたい""")
    Assertions.assertEquals("ぎょうざしゃぶしゃぶやきにくびるでながしこみたいさしみなべにぽんしゅでながしこみたい", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 0)
  }

  @Test
  fun testUnpronouncedWord() {
    val lyric = Lyric()
    lyric.setLyric("ヨーロッパ")
    Assertions.assertEquals("よろぱ", lyric.toString())
    Assertions.assertEquals(lyric.warnings().size, 0)
  }
}
