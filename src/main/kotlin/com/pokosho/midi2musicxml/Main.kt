package com.pokosho.midi2musicxml

import java.io.File
import java.lang.IllegalArgumentException
import javax.sound.midi.MetaMessage
import javax.sound.midi.MidiEvent
import javax.sound.midi.MidiSystem
import javax.sound.midi.ShortMessage

class Test {
  fun main(pathToMid: String, lyrics: String) {
    MidiParser().parse(pathToMid, lyrics)
  }
}

fun main() {
  Test().main("sample/120bpm_c3.mid", "しまうまたべたい")
}