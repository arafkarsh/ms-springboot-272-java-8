#!/bin/bash
KV=-3.7.0
KAFKA=~/Softwares/kafka$KV
$KAFKA/bin/kafka-server-start.sh $KAFKA/config/server.properties
