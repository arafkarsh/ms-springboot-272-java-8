#!/bin/bash
KV=-3.7.0
KAFKA=~/Softwares/kafka$KV
$KAFKA/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
