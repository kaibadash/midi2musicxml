package com.pokosho.midi2musicxml

class Test {
  fun main(pathToMid: String, lyrics: String) {
    MidiParser().generateXML(pathToMid, lyrics)
  }
}

fun main() {
  Test().main("sample/kanon_short.mid", "しまうまたべたいしまうまたべたいしまうまたべたいしまうまたべたいしまうまたべたいしまうまたべたいしまうまたべたいしまうまたべたいしまうまたべたいしまうまたべたいしまうまたべたいしまうまたべたいしまうまたべたいしまうまたべたい")
}