package com.pokosho.midi2musicxml.cui

import com.pokosho.midi2musicxml.MidiParser

/**
 * Command Line Interface.
 * See help()
 */
class Cui(val args: Array<String>) {
  fun run(): Int {
    var params: Params?
    try {
      params = Params(args, System.`in`)
    } catch (e: IllegalArgumentException) {
      System.err.println(e.message)
      println(Params.help())
      return -1
    }

    if (params.showHelp) {
      println(Params.help())
      return 0
    }

    val parser = MidiParser()
    parser.parse(params.midiFile, params.lyric())

    if (!params.silent) {
      parser.warnings().forEach { System.err.println(it) }
      parser.warnings().forEach { System.err.println(it) }
    }

    parser.generateXML(params.outputPath)
    println("Completed: ${params.outputPath}")
    // TODO: call neutrino and set output
    if (!params.callNeutrino) {
      return 0
    }
    return 0
  }
}

fun main(args: Array<String>) {
  Cui(args).run()
}