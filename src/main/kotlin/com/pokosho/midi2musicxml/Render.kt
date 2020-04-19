package com.pokosho.midi2musicxml

import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.FileWriter
import java.io.Writer

class Render() {
  @ExperimentalUnsignedTypes
  fun renderTemplate(template: String, tempo: Tempo, notes: List<Note>, outputPath: String) {
    val engine: TemplateEngine = initializeTemplateEngine()
    val context: Context = Context()
    context.setVariable("notes", notes)
    context.setVariable("tempo", tempo)
    val writer: Writer = FileWriter(outputPath)
    engine.process(template, context, writer)
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
}
