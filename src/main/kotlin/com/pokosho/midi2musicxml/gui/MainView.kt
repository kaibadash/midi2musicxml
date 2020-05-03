package com.pokosho.midi2musicxml.gui

import com.google.common.io.Files
import com.pokosho.midi2musicxml.MidiParser
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.layout.AnchorPane
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File
import java.util.*
import java.util.concurrent.Executors
import java.util.prefs.Preferences


/**
 * FIXME: 密です！
 */
class MainView {
  @FXML
  private lateinit var anchorPane: AnchorPane
  @FXML
  private lateinit var buttonSelectNeutrino: Button
  @FXML
  private lateinit var buttonSelectMidi: Button
  @FXML
  private lateinit var buttonSelectLyric: Button
  @FXML
  private lateinit var buttonPreview: Button
  @FXML
  private lateinit var buttonGenerate: Button
  @FXML
  private lateinit var textPathToNeutrino: TextField
  @FXML
  private lateinit var textPathToInputMid: TextField
  @FXML
  private lateinit var textPathToLyric: TextField
  @FXML
  private lateinit var textPreview: TextArea
  @FXML
  private lateinit var textMessage: TextArea
  private var i18nBundle = ResourceBundle.getBundle("strings", Locale.getDefault())

  @FXML
  fun initialize() {
    run {
      lazySearchNeutrino()
      loadFromPreference()

      buttonSelectNeutrino.action {
        val directoryChooser = DirectoryChooser()
        directoryChooser.title = i18nBundle.getString("ui.dialog.select_neutrino")
        directoryChooser.initialDirectory = File(textPathToNeutrino.text)
        val path = directoryChooser.showDialog(anchorPane.scene.window) ?: return@action
        textPathToInputMid.text = path.absolutePath
      }

      buttonSelectMidi.action {
        val chooser = FileChooser()
        chooser.title = i18nBundle.getString("ui.dialog.select_midi")
        chooser.extensionFilters.add(FileChooser.ExtensionFilter("MIDI", "*.mid", "*.midi", "*.MID", "*.MIDI"))
        chooser.initialDirectory = File(textPathToInputMid.text).parentFile
        val path = chooser.showOpenDialog(anchorPane.scene.window) ?: return@action
        textPathToInputMid.text = path.absolutePath
      }

      buttonSelectLyric.action {
        val chooser = FileChooser()
        chooser.title = i18nBundle.getString("ui.dialog.select_lyric")
        chooser.extensionFilters.add(FileChooser.ExtensionFilter("MIDI", "*.txt", "*.text", ".md"))
        chooser.initialDirectory = File(textPathToLyric.text).parentFile
        val path = chooser.showOpenDialog(anchorPane.scene.window) ?: return@action
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
    textMessage.textProperty().unbind()
    textMessage.text = ""
    saveToPreference()
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
    var neutrinoExt = ""
    if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
      neutrinoExt = ".exe"
      targets = listOf(
        env["USERPROFILE"] ?: "",
        env["ProgramFiles(x86)"] ?: "",
        env["ProgramFiles"] ?: "",
        env["USERPROFILE"] ?: "" + "\\Desktop").filter { it.isNotBlank() }
    }
    var path = ""
    run {
      targets.forEach {
        val dir = File(it)
        val files = dir.listFiles()
        val founds = files?.filter {
          it.name == "NEUTRINO"
        } ?: return@forEach
        if (founds.isNotEmpty() && founds.first().isDirectory) {
          if (File(founds.first().absolutePath + "/bin/NEUTRINO${neutrinoExt}").exists()) {
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
    var lyric = textPreview.text
    if (lyric.isBlank()) {
      lyric = Files.readLines(File(textPathToLyric.text), Charsets.UTF_8).joinToString("\n")
    }
    lyric = lyric.replace("\r", "").split("\n").joinToString("")
    val executorService = Executors.newSingleThreadExecutor()
    val task = NeutrinoTask(textPathToNeutrino.text, textPathToInputMid.text, lyric)
    textMessage.textProperty().bind(task.messageProperty())
    executorService.submit(task)
  }

  private fun saveToPreference() {
    val preference = Preferences.userRoot()
    listOf(textPathToNeutrino, textPathToInputMid, textPathToLyric).forEach {
      preference.put(it.id, it.text)
    }
    preference.sync()
  }

  private fun loadFromPreference() {
    val preference = Preferences.userRoot()
    listOf(textPathToNeutrino, textPathToInputMid, textPathToLyric).forEach {
      val data = preference.get(it.id, "")
      if (data.isNotBlank()) {
        it.text = data
      }
    }
  }
}

