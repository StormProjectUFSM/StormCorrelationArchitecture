#!/bin/bash

sudo ~/StormInfrastructure/ZooKeeper/zookeeper-3.4.9/bin/zkServer.sh start
nohup ~/StormInfrastructure/Kafka/kafka0.8/bin/kafka-server-start.sh ~/StormInfrastructure/Kafka/kafka0.8/config/server.properties > ~/StormInfrastructure/Kafka/kafka0.8/kafka.log &
sudo ~/StormInfrastructure/Storm/apache-storm-1.0.3/bin/storm nimbus &
sudo StormInfrastructure/Storm/apache-storm-1.0.3/bin/storm supervisor &
