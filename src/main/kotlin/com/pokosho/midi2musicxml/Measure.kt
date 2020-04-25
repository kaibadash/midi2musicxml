package com.pokosho.midi2musicxml

/**
 * 1つの小節を表す
 */
class Measure(val tickInMeasure: Int) {
  private val notes = mutableListOf<BaseNote>()

  fun addNote(note: BaseNote): Boolean {
    val sum = notes.sumBy {
      it.duration()
    }
    if (sum >= tickInMeasure) return false
    return notes.add(note)
  }

  fun notes(): List<BaseNote> {
    return notes
  }

  companion object {
    /**
     * notesを小節に分割する
     */
    fun splitMeasures(tickInMeasure: Int, notes: List<BaseNote>): List<Measure> {
      val measures = mutableListOf<Measure>()
      var measure = Measure(tickInMeasure)
      measures.add(measure)
      notes.forEach {
        if (measure.addNote(it)) return@forEach
        measure = Measure(tickInMeasure)
        measures.add(measure)
        measure.addNote(it)
      }
      return measures
    }
  }
}
