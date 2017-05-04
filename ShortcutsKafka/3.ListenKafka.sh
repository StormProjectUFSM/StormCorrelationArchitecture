#!/bin/bash

~/StormInfrastructure/Kafka/kafka0.8/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic $1 –-from-beginning
