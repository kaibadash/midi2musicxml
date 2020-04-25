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
  var notes: List<BaseNote> = mutableListOf()
  var resolution: Int = 0
  var lyric = ""
  val warnings = mutableListOf<String>()

  fun parse(pathToMidi: String, lyric: String) {
    val sequence = MidiSystem.getSequence(File(pathToMidi))
    this.resolution = sequence.resolution
    this.lyric = lyric
    val notes = arrayListOf<Note>()
    if (sequence.tracks.size > 2) {
      warnings.add("MIDI has some tracks(${sequence.tracks.size}). Midi2musicxml uses first track.")
    }
    // 0番目はmeta dataなので1番目を取得
    val track = sequence.tracks[1]

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
        val char = if (lyric.count() > notes.count()) lyric[notes.count()] else null
        notes.add(Note(message, char, event.tick.toInt()))
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
    validate()
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
    if (this.notes.count() == 0) return 0
    val notes = notes.filter { it is Note } as? List<Note> ?: return 0
    return ((notes.count { it.octave >= TOO_HIGH_OCTAVE }.toDouble() / notes.count()) * 100).toInt()
  }

  @Suppress("UNCHECKED_CAST")
  fun percentageOfTooLow(): Int {
    if (this.notes.count() == 0) return 0
    val notes = notes.filter { it is Note } as? List<Note> ?: return 0
    return ((notes.count { it.octave <= TOO_LOW_OCTAVE }.toDouble() / notes.count()) * 100).toInt()
  }

  private fun validate(): List<String> {
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