package com.pokosho.midi2musicxml

import java.io.File
import javax.sound.midi.MetaMessage
import javax.sound.midi.MidiEvent
import javax.sound.midi.MidiSystem
import javax.sound.midi.ShortMessage

class Test {
  val META_TEMPO = 0x51
  val NOTE_ON = 0x90
  val NOTE_OFF = 0x80

  @ExperimentalUnsignedTypes
  fun main(pathToMid: String, lyrics: String) {
    val sequence = MidiSystem.getSequence(File(pathToMid))
    val notes = arrayListOf<Note>()
    var tempo: Tempo? = null
    sequence.tracks.forEachIndexed { trackIndex, track ->
      for (i in 0 until track.size()) {
        val event: MidiEvent = track.get(i)
        print("@" + event.tick + " ")
        val message = event.message
        if (message is MetaMessage && message.type == META_TEMPO) {
          tempo = Tempo(message.data)
          println("Tempo ${tempo!!.tempoMicrosecondsPerQuoteNote() } MPQ")
          continue
        }
        if (!(message is ShortMessage)) {
          println("Other message: " + message.javaClass)
          continue
        }
        println("Channel: ${message.channel} ")
        // MIDIは音の終わりと始まりがイベントとして記録される
        if (message.command == NOTE_ON) {
          notes.add(Note(message, lyrics[notes.count()], event.tick.toInt()))
          continue
        }
        if (message.command == NOTE_OFF) {
          val note = notes.last()
          note.end = event.tick.toInt()
          note.calculateNoteType(tempo!!.tempoMicrosecondsPerQuoteNote())
          println(note)
          continue
        }
      }
    }
    Render().renderTemplate("template.musicxml", notes, "output/test.musicxml")
  }
}

fun main() {
  Test().main("sample/120bpm_c3.mid", "うほうほはふはふうまうま")
}