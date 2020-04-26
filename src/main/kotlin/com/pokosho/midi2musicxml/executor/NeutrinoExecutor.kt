package com.pokosho.midi2musicxml.executor

import java.io.File

class NeutrinoExecutor(val dirNuetrino: String, val pathToMusicXML: String,
                       val singer: Singer = Singer.Kiritan) {
  val Model = "KIRITAN"
  val PitchShift = "1.0"
  val FormantShift = "1.0"

  /**
   * NOTE: 今後NEUTRINO
   */
  fun execute() {
    val target = baseFileName(pathToMusicXML)
    val labelFull = "${target}-full.lab"
    val labelMono = "${target}-mono.lab"
    var builder = ProcessBuilder("${dirNuetrino}/bin/musicXMLtoLabel",
      "${target}.musicxml", labelFull, labelMono)
    val thread = Runtime.getRuntime().availableProcessors().toString()
    builder.directory(File(dirNuetrino))
    println(builder.command().joinToString(" "))
    var process = builder.start()
    process.waitFor()

    val labelTiming = "${target}-timing.lab"
    val f0 = "${target}.f0"
    val mgc = "${target}.mgc"
    val bap = "${target}.bap"
    builder = ProcessBuilder("${dirNuetrino}/bin/NEUTRINO",
      labelFull, labelTiming, f0, mgc, bap,
      "./model/${singer}/",
      "-n", thread, "-t"
    )
    builder.directory(File(dirNuetrino))
    process.waitFor()
    println(builder.command().joinToString(" "))
    process = builder.start()

    val output = "${target}.wav"
    builder = ProcessBuilder("${dirNuetrino}/bin/WORLD",
      f0, mgc, bap, "-f", PitchShift, "-m", FormantShift,
      "-o", output, "-n", thread, "-t"
    )
    builder.directory(File(dirNuetrino))
    println(builder.command().joinToString(" "))
    process = builder.start()
    process.waitFor()
  }

  private fun baseFileName(path: String): String {
    val file = File(path)
    return "${file.absolutePath.split("/").dropLast(1).joinToString("/").replace("./", "")}/${file.name.split(".").dropLast(1).joinToString("")}"
  }
}