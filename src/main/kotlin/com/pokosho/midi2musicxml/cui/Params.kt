package com.pokosho.midi2musicxml.cui

import com.pokosho.midi2musicxml.Lyric
import java.io.File
import java.io.InputStream
import java.lang.IllegalArgumentException

class Params(args: Array<String>, inputStream: InputStream) {
  var showHelp = false
  var callNeutrino = false
  var silent = false
  var lyricFile: String = ""
  var midiFile: String = ""
  var warnings = mutableListOf<String>()
  var lyric: Lyric? = null
  var outputPath: String? = null

  init {
    if (args.size <= 1) {
      showHelp = true
      throw IllegalArgumentException("Specify a path to midi file.")
    }
    if (args.filter { it == "--help" }.isNotEmpty()) {
      showHelp = true
    }

    this.midiFile = args[1]
    if (!File(midiFile).exists()) {
      throw IllegalArgumentException("A midi file ${args[1]} is not found.")
    }
    this.lyricFile = getOptionValue(args, "-t")
    if (lyricFile.isNotBlank() && !File(lyricFile).exists()) {
      throw IllegalArgumentException("A text file ${lyricFile} is not found.")
    }
    this.callNeutrino = args.filter { it == "-n" }.isNotEmpty()
    this.silent = args.filter { it == "-s" }.isNotEmpty()
    this.outputPath = getOptionValue(args, "-o")
    analyzeLyric(inputStream)
  }

  fun lyric(): String {
    this.lyric ?: throw IllegalArgumentException("Lyric is not set")
    return lyric.toString()
  }

  private fun getOptionValue(args: Array<String>, option: String): String {
    args.forEachIndexed { index, it ->
      if (it == option) {
        return args.elementAtOrElse(index + 1) { "" }
      }
    }
    return ""
  }

  private fun analyzeLyric(inputStream: InputStream) {
    this.lyric = if (lyricFile.isNotBlank()) {
      Lyric(lyricFile)
    } else {
      Lyric(inputStream)
    }
    if (silent) return
    this.warnings.addAll(
      (this.lyric ?: throw IllegalArgumentException("Lyric is not set")).warnings()
    )
  }
}