#!/bin/bash

# exmample: ./run.sh output/test.musicxml

# Project settings
NEUTRINO_DIR=/Applications/NEUTRINO/0.1
BASENAME=test
NumThreads=3

# musicXML_to_label
SUFFIX=musicxml

# NEUTRINO
ModelDir=KIRITAN

# WORLD
PitchShift=1.0
FormantShift=1.0

input="`pwd`/$1"
cd $NEUTRINO_DIR

echo "input: $input"

echo "`date +"%M:%S.%2N"` : start MusicXMLtoLabel"
./bin/musicXMLtoLabel $input score/label/full/${BASENAME}.lab score/label/mono/${BASENAME}.lab

echo "`date +"%M:%S.%2N"` : start NEUTRINO"
./bin/NEUTRINO score/label/full/${BASENAME}.lab score/label/timing/${BASENAME}.lab ./output/${BASENAME}.f0 ./output/${BASENAME}.mgc ./output/${BASENAME}.bap ./model/${ModelDir}/ -n ${NumThreads} -t

echo "`date +"%M:%S.%2N"` : start WORLD"
./bin/WORLD output/${BASENAME}.f0 output/${BASENAME}.mgc output/${BASENAME}.bap -f ${PitchShift} -m ${FormantShift} -o $input.wav -n ${NumThreads} -t

echo "`date +"%M:%S.%2N"` : END"
