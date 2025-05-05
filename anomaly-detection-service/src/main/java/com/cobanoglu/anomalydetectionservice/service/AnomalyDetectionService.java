package com.cobanoglu.anomalydetectionservice.service;

import com.cobanoglu.anomalydetectionservice.model.AirQualityResponse;
import com.cobanoglu.anomalydetectionservice.model.Anomaly;

import java.util.List;
import java.util.Optional;

public interface AnomalyDetectionService {
    void checkForAnomalies(AirQualityResponse response);
    List<Anomaly> getAllAnomalies();
    List<Anomaly> getAnomaliesBetween(long start, long end);
    Optional<Anomaly> getLatestAnomaly();
    Anomaly getAnomalyById(Long id);
}
