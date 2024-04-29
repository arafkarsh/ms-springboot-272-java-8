#!/bin/bash
KAFKA=`envkafka.sh`
$KAFKA/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
