package com.pokosho.midi2musicxml.gui

import com.pokosho.midi2musicxml.MidiParser
import com.pokosho.midi2musicxml.executor.NeutrinoExecutor
import javafx.concurrent.Task

class NeutrinoTask(
  private val neutrinoDir: String, private val pathToMidi: String,
  private val lyric: String) : Task<Int>() {

  private var output = ""

  override fun call(): Int {
    val parser = MidiParser()
    parser.parse(pathToMidi, lyric)
    val pathToMusicXML = pathToMidi.split(".").dropLast(1).joinToString(".") + ".musicxml"
    parser.generateXML(pathToMusicXML)
    this.output = NeutrinoExecutor(neutrinoDir, pathToMusicXML).execute()
    return 0
  }

  override fun scheduled() {
    super.succeeded()
    updateMessage("Scheduled!")
  }

  override fun running() {
    super.running()
    updateMessage("Running!")
  }

  override fun succeeded() {
    super.succeeded()
    updateMessage("Succeeded!\n$output")
  }

  override fun cancelled() {
    super.cancelled()
    updateMessage("Cancelled!")
  }

  override fun failed() {
    super.failed()
    updateMessage("Failed!")
  }
}