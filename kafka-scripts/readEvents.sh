#!/bin/bash
KV=-3.7.0
KAFKA=~/Softwares/kafka$KV
#bin/kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092
$KAFKA/bin/kafka-console-consumer.sh --topic $1 --from-beginning --bootstrap-server localhost:9092
