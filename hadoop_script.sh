#!/usr/bin/env bash

# create directories on hdfs
hadoop fs -mkdir -p /KMeans/Resources/Input
hadoop fs -mkdir -p /KMeans/Resources/Output

# copy local input files
hadoop fs -put ./Resources/Input/points.txt ./Resources/Input/clusters.txt /KMeans/Resources/Input/

# remove output files if any
hadoop fs -rm -r -f /KMeans/Resources/Output/*

# specify input parameters
JAR_PATH=./executable_jar/kmeans_mapreduce.jar
MAIN_CLASS=Main
INPUT_FILE_PATH=/KMeans/Resources/Input/points.txt
STATE_PATH=/KMeans/Resources/Input/clusters.txt
NUMBER_OF_REDUCERS=3
OUTPUT_DIR=/KMeans/Resources/Output
DELTA=100000000.0
MAX_ITERATIONS=10
DISTANCE=eucl

hadoop jar ${JAR_PATH} ${MAIN_CLASS} --input ${INPUT_FILE_PATH} \
--state ${STATE_PATH} \
--number ${NUMBER_OF_REDUCERS} \
--output ${OUTPUT_DIR} \
--delta ${DELTA} \
--max ${MAX_ITERATIONS} \
--distance ${DISTANCE}

# execute jar file
LAST_DIR="$(hadoop fs -ls -t -C /KMeans/Resources/Output | head -1)"

# print results
hadoop fs -cat "$LAST_DIR/part-r-[0-9][0-9][0-9][0-9][0-9]" | sort --numeric --key 1
