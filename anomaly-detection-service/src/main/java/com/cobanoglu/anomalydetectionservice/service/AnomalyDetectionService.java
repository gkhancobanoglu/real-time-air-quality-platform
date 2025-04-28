package com.cobanoglu.anomalydetectionservice.service;

import com.cobanoglu.anomalydetectionservice.model.AirQualityResponse;
import com.cobanoglu.anomalydetectionservice.model.Anomaly;

import java.util.List;

public interface AnomalyDetectionService {
    void checkForAnomalies(AirQualityResponse response);
    List<Anomaly> getAllAnomalies();
}
