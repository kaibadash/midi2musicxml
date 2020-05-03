package com.pokosho.midi2musicxml.executor

import java.io.File

class NeutrinoExecutor(val dirNuetrino: String, val pathToMusicXML: String,
                       val singer: Singer = Singer.Kiritan,
                       val pitchShift: Double = 1.0, val formantShift: Double = 1.0) {
  /**
   * NOTE: 今後NEUTRINOのインタフェースがどうなるかわからないためベタ書き
   */
  fun execute(): String {
    val target = baseFileName(pathToMusicXML)

    val labelFull = "${target}-full.lab"
    val labelMono = "${target}-mono.lab"
    var builder = ProcessBuilder("${dirNuetrino}/bin/musicXMLtoLabel",
      "${target}.musicxml", labelFull, labelMono)
    val thread = Runtime.getRuntime().availableProcessors().toString()
    builder.directory(File(dirNuetrino))
    builder.inheritIO()
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
    builder.inheritIO()
    println(builder.command().joinToString(" "))
    process = builder.start()
    process.waitFor()

    val output = "${target}.wav"
    builder = ProcessBuilder("${dirNuetrino}/bin/WORLD",
      f0, mgc, bap, "-f", pitchShift.toString(), "-m", formantShift.toString(),
      "-o", output, "-n", thread, "-t"
    )
    builder.directory(File(dirNuetrino))
    builder.inheritIO()
    println(builder.command().joinToString(" "))
    process = builder.start()
    process.waitFor()
    return output
  }

  /**
   * /path/to/my_file.ext => /path/to/my_file
   */
  private fun baseFileName(path: String): String {
    val file = File(path)
    val dir = file.absolutePath.split(File.separator).dropLast(1).joinToString(File.separator)
    // /path/to/./file => /path/to/file
    val cleanDir = dir.replace(".${File.separator}", "")
    return "${cleanDir}${File.separator}${file.name.split(".")
      .dropLast(1).joinToString("")}"
  }
}