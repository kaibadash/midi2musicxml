package com.pokosho.midi2musicxml

import javax.sound.midi.ShortMessage

/**
 * 1つの音符を表す
 */
class Note(message: ShortMessage, lyric: Char, start: Int, end: Int = 0) : BaseNote(start, end) {
  var lyric: Char = 'あ'
  var key: Int = 0
  var octave: Int = 0
  var note: Int = 0
  var velocity: Int = 0

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

  override fun toString(): String {
    return "Note: ${noteName()} ${lyric} octave: $octave key: $key velocity: $velocity start: $start end: $end type: ${this.noteType.name}"
  }
}
