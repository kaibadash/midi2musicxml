package com.pokosho.midi2musicxml.gui

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import tornadofx.*
import java.util.*

class Midi2MusicXMLGUI : App(MainView::class, Styles::class) {
  override fun start(primaryStage: Stage) {
    val i18nBundle = ResourceBundle.getBundle("strings", Locale.getDefault())
    val page: AnchorPane = FXMLLoader.load<Any>(
      this.javaClass.getResource("/fxml/main_view.fxml"), i18nBundle
    ) as AnchorPane
    primaryStage.scene = Scene(page)
    primaryStage.title = i18nBundle.getString("ui.title")
    primaryStage.show()
  }
}