#!/bin/bash

rm RegexInterpreter.jar
rm -rf build

# Requires that the nesting depth for all files stays the same.
javac -d build src/*/*.java
cd build

# Requires that the nesting depth for all files stays the same.
jar cmvf ../MANIFEST.MF ../RegexInterpreter.jar */*.class

# If you increase the folder nesting depth then you need to add */*/*/.class after the jar command. 1 '/*' for each increase in depth.
# Note that an old class file will not get rewritten if the code in the edited file does not compile.
