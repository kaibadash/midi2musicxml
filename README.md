# midi2musicxml
![](https://github.com/kaibadash/midi2musicxml/workflows/build/badge.svg)

This is a converter from MIDI and Lyric text to musicxml for [NEUTRINO](https://n3utrino.work/).
It's on WIP but you can try and contribute :)

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

WIP

## Download

WIP: You can get an executable jar from [CI](https://github.com/kaibadash/midi2musicxml/actions?query=workflow%3Apackage).

## Contributing

As you like!
You can post issues and pull requests.

## License

MIT License. Copyright 2020 kaiba.
