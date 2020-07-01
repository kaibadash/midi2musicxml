package com.pokosho.midi2musicxml.gui

import javafx.application.Platform
import javafx.scene.control.TextArea
import java.io.IOException
import java.io.OutputStream

// @see https://stackoverflow.com/questions/14706674/system-out-println-to-jtextarea
class TextAreaOutputStream(private val textArea: TextArea) : OutputStream() {
    private val sb = StringBuilder()
    override fun flush() {}
    override fun close() {}

    @Throws(IOException::class)
    override fun write(b: Int) {
        if (b == '\r'.toInt()) return
        if (b == '\n'.toInt()) {
            Platform.runLater {
                textArea.appendText(sb.toString())
                sb.setLength(0)
            }
        }
        sb.append(b.toChar())
    }
}