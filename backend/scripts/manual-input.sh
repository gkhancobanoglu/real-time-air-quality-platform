#!/bin/bash

if [ $# -ne 10 ]; then
  echo "Kullanım: ./manual-input.sh <lat> <lon> <co> <no> <no2> <o3> <so2> <pm2_5> <pm10> <nh3>"
  exit 1
fi

LAT="$1"
LON="$2"
CO="$3"
NO="$4"
NO2="$5"
O3="$6"
SO2="$7"
PM25="$8"
PM10="$9"
NH3="${10}"

curl -X POST "http://localhost:8081/api/air/data" \
  -H "Content-Type: application/json" \
  -d "$(cat <<EOF
{
  "latitude": $LAT,
  "longitude": $LON,
  "co": $CO,
  "no": $NO,
  "no2": $NO2,
  "o3": $O3,
  "so2": $SO2,
  "pm2_5": $PM25,
  "pm10": $PM10,
  "nh3": $NH3
}
EOF
)"
