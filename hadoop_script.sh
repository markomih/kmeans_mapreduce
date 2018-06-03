#!/usr/bin/env bash
# create directory on hdfs
hadoop fs -mkdir -p /KMeans/Resources/Input
hadoop fs -mkdir -p /KMeans/Resources/Output
#
## copy local input files
hadoop fs -put ./Resources/Input/points.txt ./Resources/Input/clusters.txt /KMeans/Resources/Input/
#
#
## move local jar file
hadoop fs -put ./executable_jar/kmeans_mapreduce.jar /KMeans/
#
hadoop fs -rm -r -f /KMeans/Resources/Output/*


JAR_PATH=/KMeans/kmeans_mapreduce.jar
STATE_PATH=/KMeans/Resources/Input/clusters.txt
NUMBER_OF_REDUCERS=3
OUTPUT_DIR=/KMeans/Resources/Output
DELTA=100000000.0
MAX_ITERATIONS=10
DISTANCE=eucl

hadoop jar ./executable_jar/kmeans_mapreduce.jar Main --input /KMeans/Resources/Input/points.txt \
--state ${STATE_PATH} \
--number ${NUMBER_OF_REDUCERS} \
--output ${OUTPUT_DIR} \
--delta ${DELTA} \
--max ${MAX_ITERATIONS} \
--distance ${DISTANCE}

LAST_DIR="$(hadoop fs -ls -t -C /KMeans/Resources/Output | head -1)"
hadoop fs -cat "$LAST_DIR/part-r-[0-9][0-9][0-9][0-9][0-9]" | sort --numeric --key 1


# execute jar file

# print results
