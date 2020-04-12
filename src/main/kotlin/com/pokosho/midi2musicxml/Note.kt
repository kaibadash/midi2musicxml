package com.pokosho.midi2musicxml

import javax.sound.midi.ShortMessage

/**
 * 1つの音符を表す
 */
class Note(message: ShortMessage, lyric: Char, start: Int, end: Int = 0) {
  var lyric: Char = 'あ'
  var key: Int = 0
  var octave: Int = 0
  var note: Int = 0
  var velocity: Int = 0
  var start: Int = 0
  var end: Int = 0
  var noteType: NoteType = NoteType.NOTE_TYPE_QUARTER

  private val NOTE_NAMES = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")

  init {
    this.lyric = lyric
    this.start = start
    this.end = end
    key = message.data1
    octave = key / NOTE_NAMES.count() - 1
    note = key % NOTE_NAMES.count()
    velocity = message.data2
  }

  fun noteName(): String {
    return NOTE_NAMES[note]
  }

  fun calculateNoteType(tempoMicrosecondsPerQuoteNote: Int) {
    val bpm = 60000000 / tempoMicrosecondsPerQuoteNote.toDouble()
    this.noteType = NoteType.toNoteType((end - start) / 4 / bpm)
  }

  override fun toString(): String {
    return "Note: ${noteName()} ${lyric} octave: $octave key: $key velocity: $velocity start: $start end: $end type: ${this.noteType.name}"
  }
}
