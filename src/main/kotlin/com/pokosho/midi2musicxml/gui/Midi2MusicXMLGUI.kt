package com.pokosho.midi2musicxml.gui

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import tornadofx.*
import java.util.*

class Midi2MusicXMLGUI : App() {
  override fun start(stage: Stage) {
    val i18nBundle = ResourceBundle.getBundle("strings", Locale.getDefault())
    val page: AnchorPane = FXMLLoader.load<Any>(
      this.javaClass.getResource("/fxml/main_view.fxml"), i18nBundle
    ) as AnchorPane
    stage.scene = Scene(page)
    stage.title = i18nBundle.getString("ui.title")
    stage.isResizable = false
    stage.icons?.add(Image(MainViewController::class.java.getResourceAsStream("/crab.png")))

    stage.show()
  }
}