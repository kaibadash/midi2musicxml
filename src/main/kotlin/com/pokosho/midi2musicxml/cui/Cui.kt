package com.pokosho.midi2musicxml.cui

import com.pokosho.midi2musicxml.MidiParser

/**
 * Command Line Interface.
 * See help()
 */
class Cui(val args: Array<String>) {
  fun run(): Int {
    var params: Params? = null
    try {
      params = Params(args, System.`in`)
    } catch (e: IllegalArgumentException) {
      System.err.println(e.message)
      help()
      return -1
    }

    if (params.showHelp) {
      help()
      return 0
    }

    val parser = MidiParser()
    val musicXML = parser.generateXML(params.midiFile, params.lyric(), params.outputPath)
    if (!params.silent) {
      parser.warnings().forEach { System.err.println(it) }
    }
    if (!params.callNeutrino) {
      return 0
    }
    // TODO: call neutrino and set output
    return 0
  }

  private fun help() {
    println("""
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
""")
  }
}

fun main(args: Array<String>) {
  Cui(args).run()
}