package com.pokosho.midi2musicxml

abstract class BaseNote(start: Int, end: Int = 0) {
  var start: Int = 0
  var end: Int = 0
  var noteType: NoteType = NoteType.NOTE_TYPE_QUARTER

  init {
    this.start = start
    this.end = end
  }

  fun calculateNoteType(baseTick: Int) {
    this.noteType = NoteType.toNoteType((end - start) / baseTick.toDouble())
    println("${this} : ${end-start} ${this.noteType.name}")
  }

  fun duration(): Int {
    return end - start
  }
}
