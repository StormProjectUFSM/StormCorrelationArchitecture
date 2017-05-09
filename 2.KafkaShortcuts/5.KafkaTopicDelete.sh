#!/bin/bash

~/StormInfrastructure/Kafka/kafka0.8/bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic $1
echo "REMOVED TOPIC: $1" >> TOPICLOG
