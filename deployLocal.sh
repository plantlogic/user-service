#!/bin/bash
docker-compose stop && \
docker-compose rm -f && \
read -r -p "Delete database? [Y/n] " contin && \
if ! [[ "$contin" =~ ^([nN][oO]|[nN])+$ ]]
then
    rm -rf ./mongodb/
fi
mvn clean package -Dmaven.test.skip=true && \
docker-compose build && \
docker-compose up
