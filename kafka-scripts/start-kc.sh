#!/bin/bash
KV=-3.7.0
KAFKA=~/Softwares/kafka$KV
$KAFKA/bin/connect-distributed.sh $KAFKA/config/connect-distributed.properties
