package com.pokosho.midi2musicxml

class Tempo(val data: ByteArray) {
  fun tempoMicrosecondsPerQuoteNote(): Int {
    val tempo = (data[0].toUByte() * 0x10000u) +
      (data[1].toUByte() * 0x100u) +
      (data[2]).toUByte()
    return tempo.toInt()
  }

  /**
   * BPM(1分間の拍数)
   */
  fun bpm(): Int {
    val bpm = (1 * 60 * 1000 * 1000) / tempoMicrosecondsPerQuoteNote()
    return bpm.toInt()
  }
}
