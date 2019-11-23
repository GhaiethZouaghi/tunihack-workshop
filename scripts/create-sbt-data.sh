#!/bin/bash

if [ "$(docker volume ls -f name=sbt-data -q)" = "" ]; then
  echo "creating docker volume:"
  docker volume create --name=sbt-data
fi