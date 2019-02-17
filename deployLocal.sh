#!/bin/bash
docker-compose stop && \
docker-compose rm -f && \
rm -rf ./mongodb/ && \
mvn clean package && \ 
docker-compose build && \
docker-compose up
