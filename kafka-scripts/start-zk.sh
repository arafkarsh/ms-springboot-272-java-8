#!/bin/bash
KAFKA=`envkafka.sh`
$KAFKA/bin/zookeeper-server-start.sh $KAFKA/config/zookeeper.properties
