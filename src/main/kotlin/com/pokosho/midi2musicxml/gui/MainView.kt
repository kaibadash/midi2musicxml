package com.pokosho.midi2musicxml.gui

import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import tornadofx.*

class MainView : View("MainView") {
  override val root: AnchorPane by fxml("/fxml/main_view.fxml")
  private val buttonLazySearch: Button by fxid("buttonLazySearch")
  private val buttonSelectNeutrino: Button by fxid("buttonSelectNeutrino")
  private val buttonSelectMidi: Button by fxid("buttonSelectMidi")
  private val buttonSelectLyric: Button by fxid("buttonSelectLyric")
  private val buttonPreview: Button by fxid("buttonPreview")
  private val buttonGenerate: Button by fxid("buttonGenerate")
  private val textPathToNeutrino: TextField by fxid("textPathToNeutrino")
  private val textPathToInputMid: TextField by fxid("textPathToInputMid")
  private val textPathToLyric: TextField by fxid("textPathToLyric")
  private val textPreview: TextArea by fxid("textPreview")
  private val textMessage: TextArea by fxid("textMessage")

  init {
    buttonLazySearch.action {
      textMessage.text = "Hello TornadoFX by FXML"
    }
  }
}

