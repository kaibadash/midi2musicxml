package com.pokosho.midi2musicxml

import com.pokosho.midi2musicxml.cui.Cui
import com.pokosho.midi2musicxml.gui.Midi2MusicXMLGUI
import javafx.application.Application

fun main(args: Array<String>) {
  if (args.count() == 0) {
    Application.launch(Midi2MusicXMLGUI::class.java, *args)
    return
  }
  Cui(args).run()
}