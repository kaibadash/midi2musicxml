package com.pokosho.midi2musicxml

import java.io.File
import javax.sound.midi.MetaMessage
import javax.sound.midi.MidiEvent
import javax.sound.midi.MidiSystem
import javax.sound.midi.ShortMessage

class MidiParser {
  val META_TEMPO = 0x51
  val NOTE_ON = 0x90
  val NOTE_OFF = 0x80

  fun parse(pathToMidi: String, lyrics: String) {
    val sequence = MidiSystem.getSequence(File(pathToMidi))
    val notes = arrayListOf<Note>()
    var tempo: Tempo? = null
    sequence.tracks.forEach { track ->
      for (i in 0 until track.size()) {
        val event: MidiEvent = track.get(i)
        print("@" + event.tick + " ")
        val message = event.message
        if (message is MetaMessage && message.type == META_TEMPO) {
          tempo = Tempo(message.data)
          println("Tempo ${tempo!!.tempoMicrosecondsPerQuoteNote()} MPQ")
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
          continue
        }
      }
    }

    val restNotes = addRestNotes(notes).map {
      it.calculateNoteType(sequence.resolution)
      it
    }

    Render().renderTemplate(
      "template.musicxml",
      tempo ?: throw IllegalArgumentException("BPM is not set."),
      restNotes,
      "output/test.musicxml"
    )
  }

  /**
   * MIDIの空白部分に休符を挿入する
   */
  fun addRestNotes(notes: List<Note>): List<BaseNote> {
    var notesWithRest = mutableListOf<BaseNote>()
    if (notes.size == 0) return notes
    // 先頭が空白のケースを潰しておく
    if (notes.first().start > 0) {
      notesWithRest.add(RestNote(0, notes.first().end))
    }

    notesWithRest.add(notes.first())
    for (i in 1..notes.size - 1) {
      val before = notes[i - 1]
      val current = notes[i]
      if (before.end != current.start) {
        notesWithRest.add(RestNote(before.end, current.start))
      }
      notesWithRest.add(current)
    }
    return notesWithRest
  }
}