package com.pokosho.midi2musicxml.gui

import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
  companion object {
    val heading by cssclass()
  }

  init {
    label and heading {
      padding = box(10.px)
      fontSize = 20.px
      fontWeight = FontWeight.BOLD
    }
  }
}