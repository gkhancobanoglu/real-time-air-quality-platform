package com.cobanoglu.anomalydetectionservice.controller;

import com.cobanoglu.anomalydetectionservice.model.Anomaly;
import com.cobanoglu.anomalydetectionservice.service.AnomalyDetectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anomalies")
@RequiredArgsConstructor
@Tag(name = "Anomaly API", description = "Provides endpoints for detected air quality anomalies")
public class AnomalyController {

    private final AnomalyDetectionService anomalyDetectionService;

    @GetMapping
    @Operation(
            summary = "Get all anomalies or anomalies within a time range",
            description = "Returns all detected anomalies. If 'start' and 'end' timestamps are provided, returns anomalies within that time range."
    )
    public ResponseEntity<List<Anomaly>> getAnomalies(
            @Parameter(description = "Start timestamp (epoch millis)") @RequestParam(required = false) Long start,
            @Parameter(description = "End timestamp (epoch millis)") @RequestParam(required = false) Long end
    ) {
        if (start != null && end != null) {
            return ResponseEntity.ok(anomalyDetectionService.getAnomaliesBetween(start, end));
        }
        return ResponseEntity.ok(anomalyDetectionService.getAllAnomalies());
    }

    @GetMapping("/latest")
    @Operation(
            summary = "Get the latest anomaly",
            description = "Returns the most recently detected air quality anomaly"
    )
    public ResponseEntity<Anomaly> getLatestAnomaly() {
        return anomalyDetectionService.getLatestAnomaly()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get anomaly by ID",
            description = "Returns details of a specific anomaly given its ID"
    )
    public ResponseEntity<Anomaly> getAnomalyById(
            @Parameter(description = "Anomaly ID") @PathVariable Long id) {
        Anomaly anomaly = anomalyDetectionService.getAnomalyById(id);
        return ResponseEntity.ok(anomaly);
    }
}
