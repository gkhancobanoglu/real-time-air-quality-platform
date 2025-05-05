package com.cobanoglu.anomalydetectionservice.service.impl;

import com.cobanoglu.anomalydetectionservice.model.AirQualityResponse;
import com.cobanoglu.anomalydetectionservice.model.Anomaly;
import com.cobanoglu.anomalydetectionservice.repository.AnomalyRepository;
import com.cobanoglu.anomalydetectionservice.service.AnomalyDetectionService;
import com.cobanoglu.anomalydetectionservice.util.AnomalyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnomalyDetectionServiceImpl implements AnomalyDetectionService {

    private final AnomalyRepository anomalyRepository;

    @Value("${anomaly-detection.threshold.aqi}")
    private int aqiThreshold;

    @Override
    public void checkForAnomalies(AirQualityResponse response) {
        if (response == null || response.getList() == null) {
            return;
        }

        response.getList().forEach(data -> {
            if (data.getMain() == null || data.getComponents() == null) return;

            Double aqi = data.getMain().getAqi();
            Double pm25 = data.getComponents().getPm2_5();
            Double pm10 = data.getComponents().getPm10();
            Double o3 = data.getComponents().getO3();

            List<String> reasons = new ArrayList<>();

            if ((aqi != null && aqi >= aqiThreshold) || AnomalyUtils.isThresholdExceeded(data)) {
                reasons.add("Threshold exceeded");
            }

            Instant now = Instant.now();
            Instant last24Hours = now.minus(24, ChronoUnit.HOURS);
            List<Anomaly> recentAnomalies = anomalyRepository.findByTimestampBetween(last24Hours.toEpochMilli(), now.toEpochMilli());

            if (!recentAnomalies.isEmpty() && pm25 != null) {
                double avgPm25 = recentAnomalies.stream().mapToDouble(Anomaly::getAqi).average().orElse(0);
                double stdDevPm25 = calculateStandardDeviation(recentAnomalies, avgPm25);

                double zScore = AnomalyUtils.calculateZScore(pm25, avgPm25, stdDevPm25);
                if (Math.abs(zScore) > 2) {
                    reasons.add("Z-Score anomaly");
                }

                if (AnomalyUtils.isSignificantIncrease(pm25, avgPm25)) {
                    reasons.add("Significant increase");
                }

                List<Anomaly> nearbyAnomalies = findNearbyAnomalies(recentAnomalies, data.getLat(), data.getLon(), 25);
                if (AnomalyUtils.hasRegionalAnomaly(nearbyAnomalies, pm25)) {
                    reasons.add("Regional anomaly detected");
                }
            }

            if (!reasons.isEmpty()) {
                String description = String.join("; ", reasons);

                Anomaly anomaly = Anomaly.builder()
                        .lat(data.getLat())
                        .lon(data.getLon())
                        .timestamp(data.getDt() != null ? data.getDt() : Instant.now().toEpochMilli())
                        .aqi(aqi != null ? aqi : -1)
                        .description(description)
                        .build();

                anomalyRepository.save(anomaly);
            }
        });
    }

    @Override
    public List<Anomaly> getAllAnomalies() {
        return anomalyRepository.findAll();
    }

    @Override
    public List<Anomaly> getAnomaliesBetween(long start, long end) {
        return anomalyRepository.findByTimestampBetween(start, end);
    }

    @Override
    public Optional<Anomaly> getLatestAnomaly() {
        return anomalyRepository.findTopByOrderByTimestampDesc();
    }

    @Override
    public Anomaly getAnomalyById(Long id) {
        return anomalyRepository.findById(id).orElse(null);
    }

    public double calculateStandardDeviation(List<Anomaly> anomalies, double mean) {
        double variance = anomalies.stream()
                .mapToDouble(a -> Math.pow(a.getAqi() - mean, 2))
                .average()
                .orElse(0);
        return Math.sqrt(variance);
    }

    public List<Anomaly> findNearbyAnomalies(List<Anomaly> anomalies, double lat, double lon, double radiusKm) {
        List<Anomaly> nearby = new ArrayList<>();
        for (Anomaly anomaly : anomalies) {
            double distance = AnomalyUtils.calculateDistanceKm(lat, lon, anomaly.getLat(), anomaly.getLon());
            if (distance <= radiusKm) {
                nearby.add(anomaly);
            }
        }
        return nearby;
    }

}
