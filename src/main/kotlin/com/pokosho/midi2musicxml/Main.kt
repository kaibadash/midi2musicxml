package com.pokosho.midi2musicxml

import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.context.IContext
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.File
import java.io.FileWriter
import java.io.Writer
import javax.sound.midi.MetaMessage
import javax.sound.midi.MidiEvent
import javax.sound.midi.MidiSystem
import javax.sound.midi.ShortMessage

class Test {
  val META_TEMPO = 0x51
  val NOTE_ON = 0x90
  val NOTE_OFF = 0x80

  @ExperimentalUnsignedTypes
  fun test(pathToMid: String) {
    val sequence = MidiSystem.getSequence(File(pathToMid))
    val notes = arrayListOf<Note>()
    var tempo: Tempo? = null
    sequence.tracks.forEach { track ->
      for (i in 0 until track.size()) {
        val event: MidiEvent = track.get(i)
        print("@" + event.tick + " ")
        val message = event.message
        if (message is MetaMessage && message.type == META_TEMPO) {
          tempo = Tempo(message.data)
          println("Tempo ${tempo!!.tempoMicrosecondsPerQuoteNote() } MPQ")
          continue
        }
        if (!(message is ShortMessage)) {
          println("Other message: " + message.javaClass)
          continue
        }
        println("Channel: ${message.channel} ")
        // MIDIは音の終わりと始まりがイベントとして記録される
        if (message.command == NOTE_ON) {
          notes.add(Note(message, event.tick.toInt()))
          continue
        }
        if (message.command == NOTE_OFF) {
          val note = notes.last()
          note.end = event.tick.toInt()
          note.calculateNoteType(tempo!!.tempoMicrosecondsPerQuoteNote())
          println(note)
          continue
        }
      }
    }
  }

  fun renderTemplate(args: Array<String>) {
    val engine: TemplateEngine = initializeTemplateEngine()
    val ctx: IContext = makeContext(args)
    val writer: Writer = FileWriter("template/template.musicxml.xml")
    engine.process("sample", ctx, writer)
    writer.close()
  }

  private fun initializeTemplateEngine(): TemplateEngine {
    val templateEngine = TemplateEngine()
    val resolver = ClassLoaderTemplateResolver()
    resolver.setTemplateMode("XML")
    resolver.prefix = "template/"
    resolver.suffix = ".xml"
    templateEngine.setTemplateResolver(resolver)
    return templateEngine
  }

  private fun makeContext(args: Array<String>): Context {
    val context: Context = Context()
    val values = arrayOf("aaa", "bbb")
    context.setVariable("args", values)
    return context
  }
}

fun main() {
  Test().test("sample/120bpm.mid")
}