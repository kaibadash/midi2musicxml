package com.pokosho.midi2musicxml.cui

import com.pokosho.midi2musicxml.MidiParser
import com.pokosho.midi2musicxml.executor.NeutrinoExecutor
import org.apache.log4j.Logger

/**
 * Command Line Interface.
 * See help()
 */
class Cui(val args: Array<String>) {
  private val log: Logger = Logger.getLogger(Cui::class.java)

  fun run(): Int {
    val params: Params?
    try {
      params = Params(args, System.`in`)
    } catch (e: IllegalArgumentException) {
      log.error(e)
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
      (params.warnings + parser.warnings).forEach { System.err.println(it) }
    }

    parser.generateXML(params.outputPath)
    println("Completed: ${params.outputPath}")
    if (params.neutrinoDir.isNotBlank()) {
      NeutrinoExecutor(params.neutrinoDir, params.outputPath).execute()
    }
    return 0
  }
}