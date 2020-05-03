package com.pokosho.midi2musicxml

import java.util.*

enum class WarningType {
  INVALID_LYRIC_CHAR,
  MIDI_HASH_SOME_TRACKS,
  LYRIC_COUNT_NOT_MATCH,
  NOTES_TOO_HIGH,
  NOTES_TOO_LOW,
  NO_LYRIC
}

class Warning(val warningType: WarningType, vararg values: Any) {
  private var values: Array<String> = arrayOf()

  init {
    this.values = Arrays.asList(*values).map { it.toString() }.toTypedArray()
  }

  fun toLocalizedString(): String {
    val bundle = ResourceBundle.getBundle("strings")
    val localized = bundle.getString("warning.${warningType.name.toLowerCase()}")
    try {
      return localized.format(*values)
    } catch (e: MissingFormatArgumentException) {
      e.printStackTrace()
      return localized
    }
  }
}

/////////