#!/bin/bash

sudo ../../../../../../bin/storm jar /home/storm/StormInfrastructure/Storm/apache-storm-1.0.3/examples/storm-starter/target/storm-starter-1.0.3.jar storm.starter.TestsBase.$1 $1
