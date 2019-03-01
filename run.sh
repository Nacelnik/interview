#!/usr/bin/env bash
mvn clean package test dockerfile:build
docker-compose up