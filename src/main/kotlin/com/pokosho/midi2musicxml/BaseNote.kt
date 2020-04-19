package com.pokosho.midi2musicxml

open class BaseNote(start: Int, end: Int = 0) {
  var start: Int = 0
  var end: Int = 0
  var noteType: NoteType = NoteType.NOTE_TYPE_QUARTER

  init {
    this.start = start
    this.end = end
  }

  fun calculateNoteType(baseTick: Long) {
    this.noteType = NoteType.toNoteType(baseTick.toDouble() / (end - start))
  }
}
