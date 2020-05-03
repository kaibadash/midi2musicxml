package com.pokosho.midi2musicxml.gui

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.image.Image
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import org.apache.log4j.Logger
import tornadofx.*
import java.util.*


class Midi2MusicXMLGUI : App() {
  private val log: Logger = Logger.getLogger(Midi2MusicXMLGUI::class.java)

  override fun start(stage: Stage) {
    try {
      val i18nBundle = ResourceBundle.getBundle("strings", Locale.getDefault())
      val page: AnchorPane = FXMLLoader.load<Any>(
        this.javaClass.getResource("/fxml/main_view.fxml"), i18nBundle
      ) as AnchorPane
      stage.scene = Scene(page)
      stage.title = i18nBundle.getString("ui.title")
      stage.isResizable = false
      stage.icons?.add(Image(MainViewController::class.java.getResourceAsStream("/crab.png")))

      stage.show()
    } catch (e: Exception) {
      log.error(e)
      val alert = Alert(AlertType.INFORMATION)
      alert.title = "Error"
      alert.contentText = e.localizedMessage
      alert.showAndWait()
    }
  }
}