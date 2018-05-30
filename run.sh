#!/usr/bin/env bash
JAR_PATH=./executable_jar/kmeans_mapreduce.jar
java -jar $JAR_PATH --input ./Resources/Input/points.txt \
--state ./Resources/Input/clusters.txt \
--number 2 \
--output ./Resources/Output/ \
--delta 100000000.0 \
--max 10 \
--distance eucl
