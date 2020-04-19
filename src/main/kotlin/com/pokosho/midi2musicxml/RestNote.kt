package com.pokosho.midi2musicxml

/**
 * 1つの休符を表す
 */
class RestNote(start: Int, end: Int = 0) : BaseNote(start, end) {
  override fun toString(): String {
    return "Rest start: $start end: $end type: ${this.noteType.name}"
  }
}
