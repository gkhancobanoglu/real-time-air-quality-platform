package com.cobanoglu.datastorageservice.controller;

import com.cobanoglu.datastorageservice.model.AirQualityEntity;
import com.cobanoglu.datastorageservice.service.AirQualityStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/air-quality")
@RequiredArgsConstructor
@Tag(name = "Air Quality Storage API", description = "Provides access to stored air quality data and regional averages")
public class AirQualityStorageController {

    private final AirQualityStorageService airQualityService;

    @GetMapping
    @Operation(
            summary = "Get all air quality data",
            description = "Returns a list of all air quality records stored in the database."
    )
    public List<AirQualityEntity> getAllAirQualityData() {
        return airQualityService.getAll();
    }

    @GetMapping("/pollution/region")
    @Operation(
            summary = "Get average pollution in a region",
            description = "Returns average pollutant levels within a rectangular geographic area defined by min/max latitude and longitude."
    )
    public ResponseEntity<Map<String, Double>> getAveragePollutionInRegion(
            @RequestParam(name = "minLat") @Parameter(description = "Minimum latitude") double minLat,
            @RequestParam(name = "maxLat") @Parameter(description = "Maximum latitude") double maxLat,
            @RequestParam(name = "minLon") @Parameter(description = "Minimum longitude") double minLon,
            @RequestParam(name = "maxLon") @Parameter(description = "Maximum longitude") double maxLon
    ) {
        return ResponseEntity.ok(
                airQualityService.getAveragePollutionInRegion(minLat, maxLat, minLon, maxLon)
        );
    }
}
