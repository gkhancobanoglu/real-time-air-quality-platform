package com.cobanoglu.airdataservice.controller;

import com.cobanoglu.airdataservice.dto.AirDataRequest;
import com.cobanoglu.airdataservice.model.AirQualityResponse;
import com.cobanoglu.airdataservice.model.PollutantData;
import com.cobanoglu.airdataservice.service.AirQualityService;
import com.cobanoglu.airdataservice.service.KafkaProducerService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/data")
    public ResponseEntity<Void> submitAirData(@RequestBody AirDataRequest request) {
        AirQualityResponse.AirData data = new AirQualityResponse.AirData();
        data.setLat(request.getLatitude());
        data.setLon(request.getLongitude());
        data.setDt(System.currentTimeMillis());

        PollutantData components = new PollutantData();
        switch (request.getParameter()) {
            case "PM2.5":
                components.setPm2_5(request.getValue());
                break;
            case "PM10":
                components.setPm10(request.getValue());
                break;
            case "NO2":
                components.setNo2(request.getValue());
                break;
            case "SO2":
                components.setSo2(request.getValue());
                break;
            case "O3":
                components.setO3(request.getValue());
                break;
        }

        data.setComponents(components);

        AirQualityResponse.MainData main = new AirQualityResponse.MainData();
        main.setAqi(1);
        data.setMain(main);

        AirQualityResponse response = new AirQualityResponse();
        List<AirQualityResponse.AirData> list = new ArrayList<>();
        list.add(data);
        response.setList(list);

        kafkaProducerService.sendAirQualityData(response);
        return ResponseEntity.ok().build();
    }



}
