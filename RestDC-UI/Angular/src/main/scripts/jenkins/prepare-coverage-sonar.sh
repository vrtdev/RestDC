#!/bin/bash
WORKINGDIR="undefined"

while getopts w: option
do
    case "${option}"
    in
        w) WORKINGDIR=${OPTARG};;
    esac
done
OLD="./src/main/js/"
## Escape path for sed using bash find and replace
OLD="${OLD//\//\\/}"

NEW=""

for i in ${WORKINGDIR}/target/coverage/* ; do
  if [ -d "$i" ]; then
    echo "$i"
    COVERAGEFILE="$i/lcov.info"
    SONARCOVERAGEFILE="$i/lcov-sonar.info"

    sed "s/$OLD/$NEW/g" "$COVERAGEFILE" > "$SONARCOVERAGEFILE"
  fi
done




