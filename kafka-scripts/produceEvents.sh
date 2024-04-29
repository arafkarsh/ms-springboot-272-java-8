#!/bin/bash
KV=-3.7.0
KAFKA=~/Softwares/kafka$KV
#bin/kafka-console-producer.sh --topic quickstart-events --bootstrap-server localhost:9092
$KAFKA/bin/kafka-console-producer.sh --topic $1  --bootstrap-server localhost:9092
