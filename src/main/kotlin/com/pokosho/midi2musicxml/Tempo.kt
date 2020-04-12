package com.pokosho.midi2musicxml

class Tempo(val data: ByteArray) {
  @ExperimentalUnsignedTypes
  fun tempoMicrosecondsPerQuoteNote(): Int {
     val tempo = (data[0].toUByte() * 0x10000u) +
      (data[1].toUByte() * 0x100u) +
      (data[2]).toUByte()
    return tempo.toInt()
  }
}
