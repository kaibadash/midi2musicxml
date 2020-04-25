package com.pokosho.midi2musicxml

class Tempo {
  var mpq = 500000 // default 120 BPM

  constructor()
  constructor(mpqData: ByteArray) {
    this.mpq = tempoMicrosecondsPerQuoteNote(mpqData)
  }

  /**
   * BPM(1分間の拍数)
   */
  fun bpm(): Int {
    val bpm = (1 * 60 * 1000 * 1000) / this.mpq
    return bpm.toInt()
  }

  private fun tempoMicrosecondsPerQuoteNote(mpqData: ByteArray): Int {
    val tempo = (mpqData[0].toUByte() * 0x10000u) +
      (mpqData[1].toUByte() * 0x100u) +
      (mpqData[2]).toUByte()
    return tempo.toInt()
  }
}
