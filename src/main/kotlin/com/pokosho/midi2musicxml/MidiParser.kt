package com.pokosho.midi2musicxml

import org.apache.log4j.Logger
import java.io.File
import javax.sound.midi.MetaMessage
import javax.sound.midi.MidiEvent
import javax.sound.midi.MidiSystem
import javax.sound.midi.ShortMessage

class MidiParser {
  private val log: Logger = Logger.getLogger(MidiParser::class.java)
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
  var lyric: Lyric = Lyric()
  val warnings = mutableListOf<Warning>()

  fun parse(pathToMidi: String, lyricString: String) {
    val sequence = MidiSystem.getSequence(File(pathToMidi))
    this.resolution = sequence.resolution
    this.lyric = Lyric.fromString(lyricString)
    var notes = arrayListOf<BaseNote>()
    if (sequence.tracks.size > 2) {
      warnings.add(Warning(WarningType.MIDI_HASH_SOME_TRACKS, sequence.tracks.size))
    }
    // 0番目はmeta dataなので1番目を取得
    val track = if (sequence.tracks.size > 1) { sequence.tracks[1] } else { sequence.tracks[0] }

    for (i in 0 until track.size()) {
      val event: MidiEvent = track.get(i)
      log.debug("@" + event.tick + " ")
      val message = event.message
      if (message is MetaMessage && message.type == META_TEMPO) {
        this.tempo = Tempo(message.data)
        log.debug("Tempo ${tempo.mpq} MPQ")
        continue
      }
      if (!(message is ShortMessage)) {
        log.debug("Other message: " + message.javaClass)
        continue
      }
      log.debug("Channel: ${message.channel} ")
      // MIDIは音の終わりと始まりがイベントとして記録される
      if (message.command == NOTE_ON) {
        notes.add(Note(message, this.lyric.charAt(notes.count()), event.tick.toInt()))
        continue
      }
      if (message.command == NOTE_OFF) {
        val note = notes.last()
        note.end = event.tick.toInt()
        continue
      }
    }

    val notesWithRest = RestNote.addRestNotes(notes).map {
      it.calculateNoteType(resolution)
    }.toMutableList()

    if (notesWithRest.first() !is RestNote) {
      warnings.add(Warning(WarningType.START_WITH_NOTE))
      notesWithRest.add(0, RestNote.fullRest(0, resolution))
    }

    this.notes = notesWithRest
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

  fun lyricWordCount(): Int {
    return this.lyric.count()
  }

  fun lyricForPreview(): String {
    return lyric.stringForPreview()
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

  private fun validate(): List<Warning> {
    if (notesCount() != lyric.count()) {
      warnings.add(Warning(WarningType.LYRIC_COUNT_NOT_MATCH, notesCount(), lyric.count()))
    }
    if (percentageOfTooHigh() > WARN_PARCENTAGE) {
      warnings.add(Warning(WarningType.NOTES_TOO_HIGH, notesCount()))
    }
    if (percentageOfTooLow() > WARN_PARCENTAGE) {
      warnings.add(Warning(WarningType.NOTES_TOO_LOW, notesCount()))
    }
    return warnings
  }
}