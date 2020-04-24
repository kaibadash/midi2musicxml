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

  companion object {
    fun help(): String {
      return """
midi2musicxml [ path to midi ] [ STDIN ] [ options ]

OPTIONS
-t path/to/lyric_text
-o path/to/output
  Write musicxml to the specified path. By default, the output is [path to mid].musicxml.
-n
  Call NEUTRINO. You must set NEUTRINO_HOME environment.
-s
  Ignore warnings.
--help
  Print help.
"""
    }
  }

  init {
    run {
      if (args.isEmpty()) {
        showHelp = true
        throw IllegalArgumentException("Specify a path to midi file.")
      }
      if (args.filter { it == "--help" }.isNotEmpty()) {
        showHelp = true
        return@run
      }

      this.midiFile = args[0]
      if (!File(midiFile).exists()) {
        throw IllegalArgumentException("A midi file ${midiFile} is not found.")
      }
      this.lyricFile = getOptionValue(args, "-t")
      if (lyricFile.isNotBlank() && !File(lyricFile).exists()) {
        throw IllegalArgumentException("A text file ${lyricFile} is not found.")
      }
      this.callNeutrino = args.filter { it == "-n" }.isNotEmpty()
      this.silent = args.filter { it == "-s" }.isNotEmpty()
      this.outputPath = getOptionValue(args, "-o", midiFile + ".musicxml")
      if (outputPath?.endsWith(".musicxml") == false) {
        throw IllegalArgumentException("An output file ${outputPath} doesn't end with .musicxml")
      }

      analyzeLyric(inputStream)
    }
  }

  fun lyric(): String {
    this.lyric ?: throw IllegalArgumentException("Lyric is not set")
    return lyric.toString()
  }

  private fun getOptionValue(args: Array<String>, option: String, default: String = ""): String {
    args.forEachIndexed { index, it ->
      if (it == option) {
        return args.elementAtOrElse(index + 1) { default }
      }
    }
    return default
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