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
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.thymeleaf:thymeleaf:3.0.11.RELEASE")
  implementation("org.slf4j:slf4j-log4j12:1.7.30")
  implementation("com.google.guava:guava:29.0-jre")
  implementation("com.atilika.kuromoji:kuromoji-ipadic:0.9.0")
  implementation("org.openjfx:javafx-controls:14")
  implementation("no.tornado:tornadofx:1.7.17")

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
    archiveBaseName.set("shadow")
    mergeServiceFiles()
    manifest {
      attributes(mapOf("Main-Class" to "com.github.csolem.gradle.shadow.kotlin.example.App"))
    }
  }
}

tasks {
  build {
    dependsOn(shadowJar)
  }
}

javafx {
  version = "11.0.2"
  modules = listOf("javafx.controls", "javafx.graphics", "javafx.fxml")
}
