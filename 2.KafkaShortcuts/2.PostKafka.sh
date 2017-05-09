#!/bin/bash

echo "$2" | ~/StormInfrastructure/Kafka/kafka0.8/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic $1 > /dev/null
