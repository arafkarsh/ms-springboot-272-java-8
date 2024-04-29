#!/bin/bash
KV=-3.7.0
KAFKA=~/Softwares/kafka$KV
$KAFKA/bin/zookeeper-server-start.sh $KAFKA/config/zookeeper.properties
