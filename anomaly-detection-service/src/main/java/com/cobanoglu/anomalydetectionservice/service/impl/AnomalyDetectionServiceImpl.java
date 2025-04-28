package com.cobanoglu.anomalydetectionservice.service.impl;

import com.cobanoglu.anomalydetectionservice.model.AirQualityResponse;
import com.cobanoglu.anomalydetectionservice.model.Anomaly;
import com.cobanoglu.anomalydetectionservice.repository.AnomalyRepository;
import com.cobanoglu.anomalydetectionservice.service.AnomalyDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AnomalyDetectionServiceImpl implements AnomalyDetectionService {

    private final AnomalyRepository anomalyRepository;

    @Value("${anomaly-detection.threshold.aqi}")
    private int aqiThreshold;

    @Value("${anomaly-detection.threshold.pm25}")
    private double pm25Threshold;

    @Value("${anomaly-detection.threshold.o3}")
    private double o3Threshold;

    @Override
    public void checkForAnomalies(AirQualityResponse response) {
        if (response == null || response.getList() == null) {
            log.warn("Received null or empty air quality response.");
            return;
        }

        response.getList().forEach(data -> {
            if (data.getMain() == null || data.getComponents() == null) return;

            int aqi = data.getMain().getAqi() != null ? data.getMain().getAqi() : -1;
            double pm25 = data.getComponents().getPm2_5() != null ? data.getComponents().getPm2_5() : -1;
            double o3 = data.getComponents().getO3() != null ? data.getComponents().getO3() : -1;

            List<String> reasons = new ArrayList<>();
            if (aqi >= aqiThreshold) reasons.add("AQI high (" + aqi + ")");
            if (pm25 > pm25Threshold) reasons.add("PM2.5 high (" + pm25 + ")");
            if (o3 > o3Threshold) reasons.add("O3 high (" + o3 + ")");

            if (!reasons.isEmpty()) {
                String description = String.join("; ", reasons);

                Anomaly anomaly = Anomaly.builder()
                        .lat(0.0)
                        .lon(0.0)
                        .timestamp(data.getDt())
                        .aqi(aqi)
                        .description(description)
                        .build();

                anomalyRepository.save(anomaly);
                log.warn("Anomaly detected and saved: {}", anomaly);
            }
        });
    }

    @Override
    public List<Anomaly> getAllAnomalies() {
        return anomalyRepository.findAll();
    }
}
