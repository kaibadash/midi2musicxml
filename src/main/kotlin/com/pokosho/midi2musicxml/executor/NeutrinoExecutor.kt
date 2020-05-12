package com.pokosho.midi2musicxml.executor

import org.apache.log4j.Logger
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths

class NeutrinoExecutor(val dirNuetrino: String, val pathToMusicXML: String,
                       val singer: Singer = Singer.Kiritan,
                       val pitchShift: Double = 1.0, val formantShift: Double = 1.0, val outputStream: OutputStreamt) {
  private val log: Logger = Logger.getLogger(NeutrinoExecutor::class.java)

  /**
   * NOTE: 今後NEUTRINOのインタフェースがどうなるかわからないためベタ書き
   */
  fun execute(): String {
    val target = baseFileName(pathToMusicXML)
    val labelFull = "${target}-full.lab"
    val labelMono = "${target}-mono.lab"
    executeCommand("${dirNuetrino}/bin/musicXMLtoLabel",
            "${target}.musicxml", labelFull, labelMono)

    val thread = Runtime.getRuntime().availableProcessors().toString()
    val labelTiming = "${target}-timing.lab"
    val f0 = "${target}.f0"
    val mgc = "${target}.mgc"
    val bap = "${target}.bap"
    executeCommand("${dirNuetrino}/bin/NEUTRINO",
            labelFull, labelTiming, f0, mgc, bap,
            "./model/${singer}/",
            "-n", thread, "-t")

    val output = "${target}.wav"
    executeCommand("${dirNuetrino}/bin/WORLD",
            f0, mgc, bap, "-f", pitchShift.toString(), "-m", formantShift.toString(),
            "-o", output, "-n", thread, "-t")

    return output
  }

  private fun executeCommand(vararg commandWithArgs: String) {
    val builder = ProcessBuilder(*commandWithArgs)
    builder.directory(File(dirNuetrino))

    builder.environment().putIfAbsent("DYLD_LIBRARY_PATH", "${dirNuetrino}/bin")
    log.debug(builder.command().joinToString(" "))
    val process = builder.start()
    process.inputStream.transferTo(outputStream)
    process.waitFor()
    if (process.exitValue() > 0)
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