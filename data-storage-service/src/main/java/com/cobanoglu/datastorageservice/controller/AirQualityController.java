package com.cobanoglu.datastorageservice.controller;

import com.cobanoglu.datastorageservice.model.AirQualityEntity;
import com.cobanoglu.datastorageservice.service.AirQualityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/air-quality")
@RequiredArgsConstructor
public class AirQualityController {

    private final AirQualityService airQualityService;

    @GetMapping
    public List<AirQualityEntity> getAllAirQualityData() {
        return airQualityService.getAll();
    }
}
