#!/bin/bash

DURATION=${1:-30}           # saniye
RATE=${2:-2}                # saniyede kaç istek
ANOMALY_CHANCE=${3:-30}     # yüzdelik (%)

# Maksimum süre kontrolü
if (( DURATION > 600 )); then
  echo "Maksimum süre 600 saniye olabilir. 600 olarak ayarlanıyor."
  DURATION=600
fi

# Geçerli yüzdelik değer kontrolü
if (( ANOMALY_CHANCE < 0 || ANOMALY_CHANCE > 100 )); then
  echo "Anomali şansı 0-100 arasında olmalıdır. 30 olarak ayarlanıyor."
  ANOMALY_CHANCE=30
fi

echo "Başlıyor: Süre=$DURATION sn, Oran=$RATE req/s, Anomali Şansı=$ANOMALY_CHANCE%"

END=$((SECONDS + DURATION))

while [ $SECONDS -lt $END ]; do
  for ((i = 0; i < RATE; i++)); do
    LAT=$(awk 'BEGIN{srand(); printf "%.6f", -90 + rand()*180}')
    LON=$(awk 'BEGIN{srand(); printf "%.6f", -180 + rand()*360}')

    get_value() {
      local chance=$(( RANDOM % 100 ))
      if (( chance < ANOMALY_CHANCE )); then
        echo $((150 + RANDOM % 351))  # 150-500
      else
        echo $((20 + RANDOM % 31))    # 20-50
      fi
    }

    CO=$(get_value)
    NO=$(get_value)
    NO2=$(get_value)
    O3=$(get_value)
    SO2=$(get_value)
    PM25=$(get_value)
    PM10=$(get_value)
    NH3=$(get_value)

    ./manual-input.sh "$LAT" "$LON" "$CO" "$NO" "$NO2" "$O3" "$SO2" "$PM25" "$PM10" "$NH3" &
  done

  wait
  sleep 1
done
