#!/bin/bash

if [ "$(docker network ls -f name=tunihack-network -q)" = "" ]; then
  echo "Creating Docker network:"
  docker network create tunihack-network
  echo "Network created"
else
  echo "Network already exists, nothing to do."
fi
