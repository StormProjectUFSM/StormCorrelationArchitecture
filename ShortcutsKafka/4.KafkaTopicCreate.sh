#!/bin/bash

~/StormInfrastructure/Kafka/kafka0.8/bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic $1 --partitions 1 --replication-factor 1
echo "ACTIVE TOPIC: $1" >> TOPICLOG
