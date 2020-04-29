package com.pokosho.midi2musicxml.gui

import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.AnchorPane
import tornadofx.*

class MainView : View("MainView") {
  override val root: AnchorPane by fxml("/fxml/main_view.fxml")
  private val button: Button by fxid("sampleButton")
  private val textArea: TextArea by fxid("sampleTextArea")

  init {
    button.action {
      textArea.text = "Hello TornadoFX by FXML"
    }
  }
}

