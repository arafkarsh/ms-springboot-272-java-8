#!/bin/bash
KV=-3.7.0
KAFKA=~/Softwares/kafka$KV
#bin/kafka-topics.sh --create --topic quickstart-events --bootstrap-server localhost:9092
$KAFKA/bin/kafka-topics.sh --create --topic $1 --bootstrap-server localhost:9092
