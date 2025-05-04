#!/bin/bash

LAT=$1
LON=$2
PARAM=$3
VALUE=$4

if [ $# -ne 4 ]; then
  echo "Kullanım: ./manual-input.sh <latitude> <longitude> <parameter> <value>"
  exit 1
fi

curl -X POST "http://localhost:8081/api/air/data" \
  -H "Content-Type: application/json" \
  -d "{
    \"latitude\": $LAT,
    \"longitude\": $LON,
    \"parameter\": \"$PARAM\",
    \"value\": $VALUE
  }"
