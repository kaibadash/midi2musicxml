package com.pokosho.midi2musicxml

import java.io.File
import javax.sound.midi.MidiEvent
import javax.sound.midi.MidiSystem
import javax.sound.midi.ShortMessage

class Test {
  val NOTE_ON = 0x90
  val NOTE_OFF = 0x80
  val NOTE_NAMES = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")

  fun test(pathToMid: String) {
    val sequence = MidiSystem.getSequence(File(pathToMid))
    var trackNumber = 0
    for (track in sequence.getTracks()) {
      trackNumber++
      println("Track " + trackNumber + ": size = " + track.size())
      println()
      for (i in 0 until track.size()) {
        val event: MidiEvent = track.get(i)
        print("@" + event.tick + " ")
        val message = event.message
        if (message is ShortMessage) {
          val sm = message
          print("Channel: " + sm.channel + " ")
          if (sm.command == NOTE_ON) {
            val key = sm.data1
            val octave = key / 12 - 1
            val note = key % 12
            val noteName = NOTE_NAMES[note]
            val velocity = sm.data2
            println("Note on, $noteName$octave key=$key velocity: $velocity")
          } else if (sm.command == NOTE_OFF) {
            val key = sm.data1
            val octave = key / 12 - 1
            val note = key % 12
            val noteName = NOTE_NAMES[note]
            val velocity = sm.data2
            println("Note off, $noteName$octave key=$key velocity: $velocity")
          } else {
            println("Command:" + sm.command)
          }
        } else {
          println("Other message: " + message.javaClass)
        }
      }
    }
  }
}

fun main() {
  Test().test("sample/kanon_short.mid")
}