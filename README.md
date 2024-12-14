midi2musicxml is no longer maintained, please use tyouseisientool.
https://github.com/sigprogramming/tyouseisientool

# midi2musicxml

![](https://github.com/kaibadash/midi2musicxml/workflows/build/badge.svg)

This is a converter from MIDI and Lyric text to musicxml for [NEUTRINO](https://n3utrino.work/).

## Features

- Convert from a MIDI file and text file to musicxml.
- Support Kanji lyric.
- Call NEUTRINO.
- Support both GUI and CUI
- English / Japanese

## Requirements

- Java 11 and Later

## Usage

### CUI

```
midi2musicxml [midi file] [options]

OPTIONS
-t input-text-file-lyrics
  Specify a input file of lyrics. STDIN is read if the file is not specified.
-o output-file-xml
  Specify a path to the output xml file. By default, the output is [path to mid].musicxml.
-n path-to-neutrino-directory
   Call NEUTRINO if it is specified.
-s
  Ignore warnings.
--help
  Print help.
```

### GUI

![image](https://user-images.githubusercontent.com/391549/80907978-9aadb280-8d56-11ea-881b-74c176cd01a8.png)

## Download and Setup

You can download from [release page](https://github.com/kaibadash/midi2musicxml/releases).

### Mac

Just install it.

### Windows

[Read me](https://github.com/kaibadash/midi2musicxml/blob/master/windows_script/README.md)

## Build release binaries

```bash
vi build.gradle.kts
git tag v1.2.3
git push --tags
```

## Contributing

As you like! You can post issues and pull requests.

## License

MIT License. Copyright 2020 kaiba.
