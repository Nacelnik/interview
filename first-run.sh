#!/usr/bin/env bash
mvn clean package dockerfile:build

docker run -d -p 27017:27017 --name closeit-interview-db \
        -e MONGO_INITDB_ROOT_USERNAME=closeit \
	    -e MONGO_INITDB_ROOT_PASSWORD=somesecretpassword \
        mongo

docker run -d -p 5000:8080 --name closeit-interview-app closeit-interview:0.0.1