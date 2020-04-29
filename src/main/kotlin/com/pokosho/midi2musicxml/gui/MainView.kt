package com.pokosho.midi2musicxml.gui

import com.google.common.io.Files
import com.pokosho.midi2musicxml.MidiParser
import com.pokosho.midi2musicxml.executor.NeutrinoExecutor
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

/**
 * FIXME: 密です！
 */
class MainView : View("Midi2MusicXML") {
  override val root: AnchorPane by fxml("/fxml/main_view.fxml")
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
      lazySearchNeutrino()

      buttonSelectNeutrino.action {
        val directoryChooser = DirectoryChooser()
        directoryChooser.title = "ディレクトリを選択"
        val path = directoryChooser.showDialog(this.currentWindow) ?: return@action
        textPathToInputMid.text = path.absolutePath
      }

      buttonSelectMidi.action {
        val chooser = FileChooser()
        chooser.title = "MIDIを選択"
        chooser.extensionFilters.add(FileChooser.ExtensionFilter("MIDI", "*.mid", "*.midi", "*.MID", "*.MIDI"))
        val path = chooser.showOpenDialog(this.currentWindow) ?: return@action
        textPathToInputMid.text = path.absolutePath
      }

      buttonSelectLyric.action {
        val chooser = FileChooser()
        val path = chooser.showOpenDialog(this.currentWindow) ?: return@action
        textPathToLyric.text = path.absolutePath
      }

      buttonPreview.action {
        if (!validate()) {
          return@action
        }
        preview()
      }

      buttonGenerate.action {
        if (!validate()) {
          return@action
        }
        execute()
      }
    }
  }

  private fun validate(): Boolean {
    textMessage.text = ""
    listOf(textPathToNeutrino.text, textPathToLyric.text, textPathToInputMid.text).forEach {
      if (!File(it).exists()) {
        textMessage.text = "実行に必要なファイル(${textPathToLyric.text})が見つかりませんでした。"
        return false
      }
    }
    return true
  }

  private fun lazySearchNeutrino() {
    val env = System.getenv()
    var targets = listOf("/Applications", "~/Desktop", "~/Documents", "~/")
    if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
      targets = listOf(env["HOMEPATH"] ?: "", env["ProgramFiles(x86)"] ?: "", env["ProgramFiles"]
        ?: "").filter { it.isNotBlank() }
    }
    var path = ""
    run {
      targets.forEach {
        val dir = File(it)
        val files = dir.listFiles()
        val founds = files?.filter { it.name == "NEUTRINO" } ?: return@forEach
        if (founds.isNotEmpty() && founds.first().isDirectory) {
          if (File(founds.first().absolutePath + "/bin/NEUTRINO").exists()) {
            path = founds.first().absolutePath
            return@run
          }
        }
      }
    }

    if (path.isBlank()) {
      return
    }
    textPathToNeutrino.text = path
  }

  private fun preview() {
    val parser = MidiParser()
    parser.parse(
      textPathToInputMid.text,
      Files.readLines(File(textPathToLyric.text), Charsets.UTF_8).joinToString("\n"))
    textMessage.text = parser.warnings.joinToString("\n")
    textPreview.text = parser.lyricForPreview()
  }

  private fun execute() {
    val parser = MidiParser()
    var lyric = textPreview.text
    if (lyric.isBlank()) {
      lyric = Files.readLines(File(textPathToLyric.text), Charsets.UTF_8).joinToString("\n")
    }
    lyric = lyric.replace("\r", "").split("\n").joinToString("")
    parser.parse(textPathToInputMid.text, lyric)
    val pathToMusicXML = textPathToInputMid.text.split(".").dropLast(1).joinToString(".") + ".musicxml"
    parser.generateXML(pathToMusicXML)
    NeutrinoExecutor(textPathToNeutrino.text, pathToMusicXML).execute()
  }
}

