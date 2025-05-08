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

import java.util.Arrays;

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
        components.setCo(request.getCo());
        components.setNo(request.getNo());
        components.setNo2(request.getNo2());
        components.setO3(request.getO3());
        components.setSo2(request.getSo2());
        components.setPm2_5(request.getPm2_5());
        components.setPm10(request.getPm10());
        components.setNh3(request.getNh3());

        data.setComponents(components);

        AirQualityResponse.MainData main = new AirQualityResponse.MainData();
        main.setAqi(airQualityService.calculateAQI(components));
        data.setMain(main);

        AirQualityResponse response = new AirQualityResponse();
        response.setList(Arrays.asList(data));

        kafkaProducerService.sendAirQualityData(response);
        return ResponseEntity.ok().build();
    }

}
