package com.cobanoglu.anomalydetectionservice.service.impl;

import com.cobanoglu.anomalydetectionservice.exception.AnomalyNotFoundException;
import com.cobanoglu.anomalydetectionservice.exception.InvalidAirDataException;
import com.cobanoglu.anomalydetectionservice.exception.AnomalySaveException;
import com.cobanoglu.anomalydetectionservice.model.AirQualityResponse;
import com.cobanoglu.anomalydetectionservice.model.Anomaly;
import com.cobanoglu.anomalydetectionservice.model.PollutantData;
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
            throw new InvalidAirDataException("Geçersiz hava kalitesi verisi alındı (null veya boş liste)");
        }

        response.getList().forEach(data -> {
            if (data.getMain() == null || data.getComponents() == null) return;

            Double aqi = data.getMain().getAqi();
            PollutantData comp = data.getComponents();
            Double pm25 = comp.getPm2_5();

            List<String> reasons = new ArrayList<>();

            if (aqi != null && aqi >= aqiThreshold) {
                reasons.add(String.format("AQI %.1f değeri, eşik olan %d'yi aştı.", aqi, aqiThreshold));
            }

            if (AnomalyUtils.isThresholdExceeded(data)) {
                reasons.add("Kirletici seviyeleri belirlenen eşiklerin üzerinde.");
            }

            Instant now = Instant.now();
            Instant last24Hours = now.minus(24, ChronoUnit.HOURS);
            List<Anomaly> recentAnomalies = anomalyRepository.findByTimestampBetween(
                    last24Hours.toEpochMilli(), now.toEpochMilli());

            double avgPm25 = recentAnomalies.stream().mapToDouble(Anomaly::getAqi).average().orElse(0);
            double stdDevPm25 = calculateStandardDeviation(recentAnomalies, avgPm25);

            if (pm25 != null && !recentAnomalies.isEmpty()) {
                double zScore = AnomalyUtils.calculateZScore(pm25, avgPm25, stdDevPm25);
                if (Math.abs(zScore) > 2) {
                    reasons.add("PM2.5 değeri, geçmişe göre istatistiksel olarak anormal.");
                }
            }

            if (pm25 != null && AnomalyUtils.isSignificantIncrease(pm25, avgPm25)) {
                reasons.add("PM2.5 değerinde ani ve belirgin artış gözlendi.");
            }

            List<Anomaly> nearbyAnomalies = findNearbyAnomalies(recentAnomalies, data.getLat(), data.getLon(), 25);
            if (AnomalyUtils.hasRegionalAnomaly(nearbyAnomalies, pm25)) {
                reasons.add("Aynı bölgede beklenmedik farklılıklar gözlemlendi.");
            }

            if (!reasons.isEmpty()) {
                String description = String.join(" ", reasons);

                Anomaly anomaly = Anomaly.builder()
                        .lat(data.getLat())
                        .lon(data.getLon())
                        .timestamp(data.getDt() != null ? data.getDt() : Instant.now().toEpochMilli())
                        .aqi(aqi != null ? Math.round(aqi * 10.0) / 10.0 : -1)
                        .description(description)
                        .pm25(comp.getPm2_5())
                        .pm10(comp.getPm10())
                        .o3(comp.getO3())
                        .no2(comp.getNo2())
                        .so2(comp.getSo2())
                        .co(comp.getCo())
                        .nh3(comp.getNh3())
                        .build();

                try {
                    anomalyRepository.save(anomaly);
                } catch (Exception e) {
                    throw new AnomalySaveException("Anomali kaydı veritabanına kaydedilemedi", e);
                }
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
        return anomalyRepository.findById(id)
                .orElseThrow(() -> new AnomalyNotFoundException("Anomali ID " + id + " ile bulunamadı"));
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
