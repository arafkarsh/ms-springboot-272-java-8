#!/bin/bash
KAFKA=`envkafka.sh`
#bin/kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092
$KAFKA/bin/kafka-console-consumer.sh --topic $1 --from-beginning --bootstrap-server localhost:9092
