#!/usr/bin/env bash
mvn clean package dockerfile:build

docker network create closeit-interview-pv-net
docker run -d -p 27017:27017 --name closeit-interview-db mongo --network=closeit-interview-pv-net

docker run -p 5000:8080 --name closeit-interview-app closeit-interview:0.0.1 --network=closeit-interview-pv-net