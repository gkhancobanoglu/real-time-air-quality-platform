package com.cobanoglu.anomalydetectionservice.controller;

import com.cobanoglu.anomalydetectionservice.model.Anomaly;
import com.cobanoglu.anomalydetectionservice.service.AnomalyDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anomalies")
@RequiredArgsConstructor
public class AnomalyController {

    private final AnomalyDetectionService anomalyDetectionService;

    @GetMapping
    public ResponseEntity<List<Anomaly>> getAnomalies(
            @RequestParam(required = false) Long start,
            @RequestParam(required = false) Long end
    ) {
        if (start != null && end != null) {
            return ResponseEntity.ok(anomalyDetectionService.getAnomaliesBetween(start, end));
        }
        return ResponseEntity.ok(anomalyDetectionService.getAllAnomalies());
    }

    @GetMapping("/latest")
    public ResponseEntity<Anomaly> getLatestAnomaly() {
        return anomalyDetectionService.getLatestAnomaly()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anomaly> getAnomalyById(@PathVariable Long id) {
        Anomaly anomaly = anomalyDetectionService.getAnomalyById(id);
        return ResponseEntity.ok(anomaly);
    }
}
