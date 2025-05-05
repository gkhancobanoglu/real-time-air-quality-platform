package com.cobanoglu.datastorageservice.controller;

import com.cobanoglu.datastorageservice.model.AirQualityEntity;
import com.cobanoglu.datastorageservice.service.AirQualityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/air-quality")
@RequiredArgsConstructor
public class AirQualityController {

    private final AirQualityService airQualityService;

    @GetMapping
    public List<AirQualityEntity> getAllAirQualityData() {
        return airQualityService.getAll();
    }

    @GetMapping("/pollution/region")
    public ResponseEntity<Map<String, Double>> getAveragePollutionInRegion(
            @RequestParam(name = "minLat") double minLat,
            @RequestParam(name = "maxLat") double maxLat,
            @RequestParam(name = "minLon") double minLon,
            @RequestParam(name = "maxLon") double maxLon
    ) {
        return ResponseEntity.ok(airQualityService.getAveragePollutionInRegion(minLat, maxLat, minLon, maxLon));
    }

}
