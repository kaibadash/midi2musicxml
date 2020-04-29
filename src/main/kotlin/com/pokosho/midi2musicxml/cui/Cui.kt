package com.pokosho.midi2musicxml.cui

import com.pokosho.midi2musicxml.MidiParser
import com.pokosho.midi2musicxml.executor.NeutrinoExecutor
import com.pokosho.midi2musicxml.gui.Midi2MusicXMLGUI
import javafx.application.Application.launch

/**
 * Command Line Interface.
 * See help()
 */
class Cui(val args: Array<String>) {
  fun run(): Int {
    if (args.count() == 0) {
      launch(Midi2MusicXMLGUI::class.java, *args)
      return 0
    }
    val params: Params?
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

fun main(args: Array<String>) {
  Cui(args).run()
}