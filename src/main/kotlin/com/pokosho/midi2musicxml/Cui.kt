package com.pokosho.midi2musicxml

import java.io.File
import java.io.InputStream
import java.lang.IllegalArgumentException

/**
 * Command Line Interface.
 * See help()
 */
class Cui(val args: Array<String>) {
  fun run(): Int {
    try {
      parse(args)
    } catch (e: IllegalArgumentException) {
      System.err.println(e.message)
      return -1
    }
    val midiFile = args[1]
    val textFile = getOptionValue(args, "-t")
    val callNeutrino = args.filter { it == "-n" }.isNotEmpty()
    val silent = args.filter { it == "-s" }.isNotEmpty()
    val output = getOptionValue(args, "-o")

    val lyric = if (textFile.isNotBlank()) {
      Lyric(textFile)
    } else {
      Lyric(System.`in`)
    }
    if (!silent && lyric.warnings().isNotEmpty()) {
      lyric.warnings().forEach { System.err.println(it) }
    }
    val parser = MidiParser()
    val musicXML = parser.parse(midiFile, lyric.toString())
    if (!silent) {
      parser.warnings().forEach { System.err.println(it) }
    }
    if (!callNeutrino) {
      return 0
    }
    // call neutrino and set outout
    return 0
  }

  private fun help() {
    println("""
midi2musicxml [ path to midi ] [ STDIN ] [ options ]

OPTIONS
-t path/to/lyric_text
-o path/to/output
  Write musicxml to the specified path.
-n
  Call NEUTRINO. You must set NEUTRINO_HOME environment.
-s
  Ignore warnings.
--help
  Print help.
""")
  }

  // TODO: クラス化して CUI params を返す
  private fun parse(args: Array<String>) {
    if (args.size <= 1) {
      help()
      throw IllegalArgumentException("Specify a path to midi file.")
    }
    if (args.filter { it == "--help" }.isNotEmpty()) {
      help()
      return
    }
    if (!File(args[1]).exists()) {
      throw IllegalArgumentException("A midi file ${args[1]} is not found.")
    }

    val textFile = getOptionValue(args, "-t")
    if (textFile.isNotBlank() && !File(textFile).exists()) {
      throw IllegalArgumentException("A text file ${textFile} is not found.")
    }
  }

  private fun getOptionValue(args: Array<String>, option: String): String {
    args.forEachIndexed { index, it ->
      if (it == option) {
        return args.elementAtOrElse(index + 1) { "" }
      }
    }
    return ""
  }
}