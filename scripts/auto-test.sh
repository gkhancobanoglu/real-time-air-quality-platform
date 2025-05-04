#!/bin/bash

DURATION=${1:-30}           # saniye
RATE=${2:-2}                # saniyede kaç istek
ANOMALY_CHANCE=${3:-30}     # yüzdelik (%)

PARAMETERS=("PM2.5" "PM10" "NO2" "SO2" "O3")

echo "Başlıyor: Süre=$DURATION sn, Oran=$RATE req/s, Anomali Şansı=$ANOMALY_CHANCE%"

END=$((SECONDS+DURATION))

while [ $SECONDS -lt $END ]; do
  for ((i=0; i<$RATE; i++)); do
    LAT=$(awk -v min=40.9 -v max=41.1 'BEGIN{srand(); print min+rand()*(max-min)}')
    LON=$(awk -v min=28.8 -v max=29.2 'BEGIN{srand(); print min+rand()*(max-min)}')
    PARAM=${PARAMETERS[$RANDOM % ${#PARAMETERS[@]}]}

    # Normal değerler
    VALUE=$(awk 'BEGIN{srand(); print int(20 + rand()*15)}')

    # Anomali üretme şansı
    if (( RANDOM % 100 < ANOMALY_CHANCE )); then
      VALUE=$(awk 'BEGIN{srand(); print int(100 + rand()*100)}')
    fi

    ./manual-input.sh "$LAT" "$LON" "$PARAM" "$VALUE" &
  done
  sleep 1
done
