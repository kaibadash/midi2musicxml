package com.pokosho.midi2musicxml

abstract class BaseNote(start: Int, end: Int = 0) {
  var start: Int = 0
  var end: Int = 0
  var noteType: NoteType = NoteType.NOTE_TYPE_QUARTER

  init {
    this.start = start
    this.end = end
  }

  fun calculateNoteType(resolution: Int): BaseNote {
    this.noteType = NoteType.toNoteType((end - start) / resolution.toDouble())
    return this
  }

  fun duration(): Int {
    return end - start
  }
}
