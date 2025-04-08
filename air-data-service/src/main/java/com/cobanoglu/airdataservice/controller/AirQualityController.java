package com.cobanoglu.airdataservice.controller;

import com.cobanoglu.airdataservice.model.AirQualityResponse;
import com.cobanoglu.airdataservice.service.AirQualityService;
import com.cobanoglu.airdataservice.service.KafkaProducerService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/air")
@RequiredArgsConstructor
@Validated
public class AirQualityController {

    private final AirQualityService airQualityService;
    private final KafkaProducerService kafkaProducerService;

    @GetMapping
    public ResponseEntity<AirQualityResponse> getAirQuality(
            @RequestParam("lat") @NotNull Double lat,
            @RequestParam("lon") @NotNull Double lon) {

        AirQualityResponse response = airQualityService.getAirQuality(lat, lon);
        kafkaProducerService.sendAirQualityData(response);

        return ResponseEntity.ok(response);
    }
}
