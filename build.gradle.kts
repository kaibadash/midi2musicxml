import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.3.71"
}

group = "com.pokosho"
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

  testImplementation("org.mockito:mockito-junit-jupiter:3.3.3")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "1.8"
  }
}
