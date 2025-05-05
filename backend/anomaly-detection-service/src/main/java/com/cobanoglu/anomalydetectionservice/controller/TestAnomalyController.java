package com.cobanoglu.anomalydetectionservice.controller;

import com.cobanoglu.anomalydetectionservice.model.AirQualityResponse;
import com.cobanoglu.anomalydetectionservice.service.AnomalyDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestAnomalyController {

    private final AnomalyDetectionService anomalyDetectionService;

    @PostMapping("/check-anomaly")
    public ResponseEntity<String> checkAnomaly(@RequestBody AirQualityResponse airQualityResponse) {
        anomalyDetectionService.checkForAnomalies(airQualityResponse);
        return ResponseEntity.ok("Anomaly check completed.");
    }
}
