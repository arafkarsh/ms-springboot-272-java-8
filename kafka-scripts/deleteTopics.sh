#!/bin/bash
KAFKA=`envkafka.sh`
$KAFKA/bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic $1
