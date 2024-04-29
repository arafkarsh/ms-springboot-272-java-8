#!/bin/bash
KAFKA=`envkafka.sh`
$KAFKA/bin/kafka-server-start.sh $KAFKA/config/server.properties
