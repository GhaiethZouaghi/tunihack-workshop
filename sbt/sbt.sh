#!/bin/bash
set -e

if [ "$#" -eq 0 ]; then
  exec sbt sbtVersion
else
  exec "$@"
fi

