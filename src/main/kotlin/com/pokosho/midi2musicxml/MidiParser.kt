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
  // FIXME: 1小節内の4分音符の数。可変に対応する。
  val QUOTES_IN_MEASURE = 4
  val TOO_LOW_OCTAVE = 2
  val TOO_HIGH_OCTAVE = 6
  val WARN_PARCENTAGE = 50

  var tempo = Tempo()
  var notes: List<BaseNote> = mutableListOf<BaseNote>()
  var resolution: Int = 0
  var lyric = ""

  fun parse(pathToMidi: String, lyric: String) {
    val sequence = MidiSystem.getSequence(File(pathToMidi))
    this.resolution = sequence.resolution
    this.lyric = lyric
    val notes = arrayListOf<Note>()
    val track = sequence.tracks.first()

    for (i in 0 until track.size()) {
      val event: MidiEvent = track.get(i)
      print("@" + event.tick + " ")
      val message = event.message
      if (message is MetaMessage && message.type == META_TEMPO) {
        this.tempo = Tempo(message.data)
        println("Tempo ${tempo.mpq} MPQ")
        continue
      }
      if (!(message is ShortMessage)) {
        println("Other message: " + message.javaClass)
        continue
      }
      println("Channel: ${message.channel} ")
      // MIDIは音の終わりと始まりがイベントとして記録される
      if (message.command == NOTE_ON) {
        notes.add(Note(message, lyric[notes.count()], event.tick.toInt()))
        continue
      }
      if (message.command == NOTE_OFF) {
        val note = notes.last()
        note.end = event.tick.toInt()
        continue
      }
    }

    this.notes = RestNote.addRestNotes(notes).map {
      it.calculateNoteType(resolution)
    }
  }

  fun generateXML(outputPath: String) {
    Render().renderTemplate(
      "template.musicxml",
      tempo,
      Measure.splitMeasures(
        QUOTES_IN_MEASURE * resolution, notes
      ),
      outputPath
    )
  }

  fun notesCount(): Int {
    return notes.filter { it is Note }.count()
  }

  fun lyricCharCount(): Int {
    return lyric.length
  }

  @Suppress("UNCHECKED_CAST")
  fun percentageOfTooHigh(): Int {
    val notes = notes.filter { it is Note } as? List<Note> ?: return 0
    return notes.count { it.octave >= TOO_HIGH_OCTAVE } / this.notes.count() * 100
  }

  @Suppress("UNCHECKED_CAST")
  fun percentageOfTooLow(): Int {
    val notes = notes.filter { it is Note } as? List<Note> ?: return 0
    return notes.count { it.octave <= TOO_LOW_OCTAVE } / this.notes.count() * 100
  }

  fun warnings(): List<String> {
    val warnings = mutableListOf<String>()
    if (notesCount() != lyricCharCount()) {
      warnings.add("Number of notes(${notesCount()}) is not much number of lyric characters (${lyricCharCount()}).")
    }
    if (percentageOfTooHigh() > WARN_PARCENTAGE) {
      warnings.add("Many notes(${notesCount()}%) are too high. Valid octave?")
    }
    if (percentageOfTooLow() > WARN_PARCENTAGE) {
      warnings.add("Many notes(${notesCount()}%) are too low. Valid octave?")
    }
    return warnings
  }
}