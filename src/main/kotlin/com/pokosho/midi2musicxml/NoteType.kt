package com.pokosho.midi2musicxml

import java.util.*

/**
 * @see https://github.com/w3c/musicxml/blob/8bbe8e50606bf99317a86e9e1637618d6bdd1997/schema/note.mod#L223
 */
enum class NoteType(val type: String) {
  NOTE_TYPE_1024TH("1024th"),
  NOTE_TYPE_512TH("512th"),
  NOTE_TYPE_256TH("256th"),
  NOTE_TYPE_128TH("128th"),
  NOTE_TYPE_64TH("64th"),
  NOTE_TYPE_32ND("32nd"),
  NOTE_TYPE_16TH("16th"),
  NOTE_TYPE_EIGHTH("eighth"),
  NOTE_TYPE_QUARTER("quarter"), // 1 beat
  NOTE_TYPE_HALF("half"),
  NOTE_TYPE_WHOLE("whole"),
  NOTE_TYPE_BREVE("breve"),
  NOTE_TYPE_LONG("long"),
  NOTE_TYPE_MAXIMA("maxima");

  companion object {
    fun toNoteType(beat: Double): NoteType {
      val beat2note = hashMapOf<Double, NoteType>(
        0.125 to NOTE_TYPE_64TH,
        0.125 to NOTE_TYPE_32ND,
        0.25 to NOTE_TYPE_16TH,
        0.5 to NOTE_TYPE_EIGHTH,
        1.0 to NOTE_TYPE_QUARTER,
        2.0 to NOTE_TYPE_HALF,
        4.0 to NOTE_TYPE_WHOLE
      )
      // 近似値を探す
      val key = beat2note.keys.stream().min(Comparator.comparing { x: Double -> Math.abs(x - beat) }).get()
      return beat2note[key] ?: throw IllegalArgumentException("NoteType is not found against beat(${beat})")
    }
  }

  /**
   * musicxmlのdurationはeighthが1
   */
  fun durationForMusicXML(): Double {
    return when (this) {
      NOTE_TYPE_1024TH -> {
        (0.12 / 2 / 2 / 2)
      }
      NOTE_TYPE_512TH -> {
        (0.12 / 2 / 2 / 2)
      }
      NOTE_TYPE_256TH -> {
        (0.12 / 2 / 2 / 2)
      }
      NOTE_TYPE_128TH -> {
        (0.12 / 2 / 2 / 2)
      }
      NOTE_TYPE_64TH -> {
        (0.12 / 2 / 2 / 2)
      }
      NOTE_TYPE_32ND -> {
        (0.12 / 2 / 2 / 2)
      }
      NOTE_TYPE_16TH -> {
        (1.0 / 2)
      }
      NOTE_TYPE_EIGHTH -> {
        1.toDouble()
      }
      NOTE_TYPE_QUARTER -> {
        2.toDouble()
      }
      NOTE_TYPE_HALF -> {
        4.toDouble()
      }
      NOTE_TYPE_WHOLE -> {
        8.toDouble()
      }
      NOTE_TYPE_BREVE -> {
        16.toDouble()
      }
      NOTE_TYPE_LONG -> {
        32.toDouble()
      }
      NOTE_TYPE_MAXIMA -> {
        64.toDouble()
      }
    }
  }
}
