package com.pokosho.midi2musicxml.cui

import com.pokosho.midi2musicxml.Lyric
import java.io.File
import java.io.InputStream

class Params(args: Array<String>, inputStream: InputStream) {
  var showHelp = false
  var neutrinoDir = ""
  var silent = false
  var lyricFile: String = ""
  var midiFile: String = ""
  var warnings = mutableListOf<String>()
  var lyric: Lyric = Lyric()
  var outputPath: String = ""

  companion object {
    fun help(): String {
      return """
midi2musicxml [midi file] [options]

OPTIONS
-t input-text-file-lyrics
  Specify a input file of lyrics. STDIN is read if the file is not specified. 
-o output-file-xml
  Specify a path to the output xml file. By default, the output is [path to mid].musicxml.
-n path-to-neutrino-directory
   Call NEUTRINO if it is specified.
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
        throw IllegalArgumentException("A midi file $midiFile is not found.")
      }
      this.lyricFile = getOptionValue(args, "-t")
      if (lyricFile.isNotBlank() && !File(lyricFile).exists()) {
        throw IllegalArgumentException("A text file $lyricFile is not found.")
      }
      this.neutrinoDir = getOptionValue(args, "-n")
      this.silent = args.filter { it == "-s" }.isNotEmpty()
      val midi = File(midiFile)
      this.outputPath = getOptionValue(args, "-o", "${midi.parent}/${midi.name.split(".").first()}.musicxml")
      if (!outputPath.endsWith(".musicxml")) {
        throw IllegalArgumentException("An output file $outputPath doesn't end with .musicxml")
      }

      analyzeLyric(lyricFile, inputStream)
    }
  }

  fun lyric(): String {
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

  private fun analyzeLyric(lyricFile: String, inputStream: InputStream) {
    if (lyricFile.isNotBlank()) {
      this.lyric = Lyric(lyricFile)
    }
    if (inputStream.available() > 0) {
      this.lyric = Lyric(inputStream)
    }

    if (silent) return
    if (lyric.toString().isEmpty()) {
      this.warnings.add("Lyric is not set")
      return
    }
    this.warnings.addAll(lyric.warnings())
  }
}
