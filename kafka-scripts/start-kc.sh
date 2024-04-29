#!/bin/bash
KAFKA=`envkafka.sh`
$KAFKA/bin/connect-distributed.sh $KAFKA/config/connect-distributed.properties
