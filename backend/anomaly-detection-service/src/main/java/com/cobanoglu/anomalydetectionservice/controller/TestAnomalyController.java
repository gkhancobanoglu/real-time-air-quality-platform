package com.cobanoglu.anomalydetectionservice.controller;

import com.cobanoglu.anomalydetectionservice.model.AirQualityResponse;
import com.cobanoglu.anomalydetectionservice.service.AnomalyDetectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Tag(name = "Anomaly Test API", description = "Manual anomaly testing using air quality data")
public class TestAnomalyController {

    private final AnomalyDetectionService anomalyDetectionService;

    @PostMapping("/check-anomaly")
    @Operation(
            summary = "Check anomaly from request",
            description = "Receives an AirQualityResponse and triggers anomaly detection logic manually."
    )
    public ResponseEntity<String> checkAnomaly(@RequestBody AirQualityResponse airQualityResponse) {
        anomalyDetectionService.checkForAnomalies(airQualityResponse);
        return ResponseEntity.ok("Anomaly check completed.");
    }
}
