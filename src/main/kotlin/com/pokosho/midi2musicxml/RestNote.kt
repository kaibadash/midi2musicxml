package com.pokosho.midi2musicxml

/**
 * 1つの休符を表す
 */
class RestNote(start: Int, end: Int = 0) : BaseNote(start, end) {
  override fun toString(): String {
    return "Rest start: $start end: $end type: ${this.noteType.name}"
  }

  companion object {
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
}
