package com.pokosho.midi2musicxml.gui

import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File


class MainView : View("Midi2MusicXML") {
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
    run {
      this.currentStage?.isResizable = false
      buttonLazySearch.action {
        val dir = lazySearchNeutrino()
        if (dir == null) {
          textMessage.text = "NEUTRINOが見つかりませんでした"
          return@action
        }
        textPathToNeutrino.text = dir?.absolutePath
      }

      buttonSelectNeutrino.action {
        val directoryChooser = DirectoryChooser()
        directoryChooser.title = "ディレクトリを選択"
        val path = directoryChooser.showDialog(this.currentWindow) ?: return@action
        textPathToInputMid.text = path.absolutePath
      }

      buttonSelectMidi.action {
        val chooser = FileChooser()
        chooser.title = "midiを選択"
        chooser.extensionFilters.add(FileChooser.ExtensionFilter("MIDI", "*.mid", "*.midi", "*.MID", "*.MIDI"))
        val path = chooser.showOpenDialog(this.currentWindow) ?: return@action
        textPathToInputMid.text = path.absolutePath
      }

      buttonSelectLyric.action {
        val chooser = FileChooser()
        val path = chooser.showOpenDialog(this.currentWindow) ?: return@action
        textPathToLyric.text = path.absolutePath
      }

      buttonPreview.action { validate() }
    }
  }

  private fun validate() {
    textMessage.text = ""
    listOf(textPathToNeutrino.text, textPathToLyric.text, textPathToInputMid.text).forEach {
      if (!File(it).exists()) {
        textMessage.text = "実行に必要なファイル(${textPathToLyric.text})が見つかりませんでした。"
      }
    }
  }

  private fun lazySearchNeutrino(): File? {
    val env = System.getenv()
    var targets = listOf("/Applications", "~/Desktop", "~/Documents", "~/")
    if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
      targets = listOf(env["HOMEPATH"] ?: "" , env["ProgramFiles(x86)"] ?: "", env["ProgramFiles"] ?: "").filter { it.isNotBlank() }
    }
    targets.forEach {
      val dir = File(it)
      val founds = dir.listFiles().filter { it.name == "NEUTRINO" }
      if (founds.size > 0 && founds.first().isDirectory) {
        if (File(founds.first().absolutePath + "/bin/NEUTRINO").exists()) {
          return founds.first()
        }
      }
    }
    return null
  }
}

