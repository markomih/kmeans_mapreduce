#!/usr/bin/env bash

JAR_PATH=./executable_jar/kmeans_mapreduce.jar
STATE_PATH=./Resources/Input/clusters.txt
NUMBER_OF_REDUCERS=3
OUTPUT_DIR=./Resources/Output
DELTA=100000000.0
MAX_ITERATIONS=10
DISTANCE=eucl

java -jar ${JAR_PATH} --input ./Resources/Input/points.txt \
--state ${STATE_PATH} \
--number ${NUMBER_OF_REDUCERS} \
--output ${OUTPUT_DIR} \
--delta ${DELTA} \
--max ${MAX_ITERATIONS} \
--distance ${DISTANCE}

LAST_DIR="$(ls -v -r ${OUTPUT_DIR} | head -1)"
LAST_DIR_PATH="$OUTPUT_DIR/$LAST_DIR/part-r-[0-9][0-9][0-9][0-9][0-9]"
POINTS="$(cat ${LAST_DIR_PATH} | sort -n)"

tput setaf 1; echo ${POINTS}; tput sgr0
