package com.pokosho.midi2musicxml

class Test {
  fun main(pathToMid: String, lyrics: String) {
    MidiParser().parse(pathToMid, lyrics)
  }
}

fun main() {
  Test().main("sample/120bpm_c3.mid", "しまうまたべたい")
}