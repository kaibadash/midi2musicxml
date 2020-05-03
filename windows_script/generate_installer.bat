gradlew shadowJar
jpackage --main-class com.pokosho.midi2musicxml.MainKt -i build/libs -n midi2musicxml --main-jar midi2musicxml.jar --icon build/resources/main/crab.png --app-version 0.0.1
