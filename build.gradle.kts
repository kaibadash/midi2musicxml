import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  kotlin("jvm") version "1.3.71"
  id("java")
  id("com.github.johnrengelman.shadow") version "5.2.0"
  id("application")
  id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "com.pokosho.midi2musicxml"
version = "0.0.6"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val javafxVersion = "11.0.2"

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.thymeleaf:thymeleaf:3.0.11.RELEASE")
  implementation("org.slf4j:slf4j-log4j12:1.7.30")
  implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.13.2")
  implementation("org.apache.logging.log4j:log4j-core:2.13.2")
  implementation("com.google.guava:guava:29.0-jre")
  implementation("com.atilika.kuromoji:kuromoji-ipadic:0.9.0")
  implementation("org.openjfx:javafx-controls:14")
  implementation("no.tornado:tornadofx:1.7.17")
  compileOnly("org.openjfx:javafx-graphics:$javafxVersion.version:win")
  compileOnly("org.openjfx:javafx-graphics:$javafxVersion:linux")
  compileOnly("org.openjfx:javafx-graphics:$javafxVersion:mac")

  testImplementation("org.mockito:mockito-junit-jupiter:3.3.3")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf(
      "-Xjsr305=strict",
      "-Xopt-in=kotlin.ExperimentalUnsignedTypes"
    )
    jvmTarget = "11"
  }
}

tasks {
  named<ShadowJar>("shadowJar") {
    // val jarName = "midi2musicxml-${archiveVersion.get()}.${archiveExtension.get()}"
    val jarName = "midi2musicxml.jar"
    println(jarName)
    archiveFileName.set(jarName)
    mergeServiceFiles()
    manifest {
      attributes(mapOf("Main-Class" to "com.pokosho.midi2musicxml.MainKt"))
    }

    doLast {
      println(archiveFileName.get())
    }
  }
}

tasks {
  build {
    dependsOn(shadowJar)
  }
}

javafx {
  version = javafxVersion
  modules = listOf("javafx.controls", "javafx.graphics", "javafx.fxml")
}

application {
  mainClassName = "com.pokosho.midi2musicxml.MainKt"
}
