package com.cobanoglu.airdataservice.controller;

import com.cobanoglu.airdataservice.dto.AirDataRequest;
import com.cobanoglu.airdataservice.model.AirQualityResponse;
import com.cobanoglu.airdataservice.model.PollutantData;
import com.cobanoglu.airdataservice.service.AirQualityService;
import com.cobanoglu.airdataservice.service.KafkaProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Air Quality API", description = "Provides real-time air quality data using OpenWeatherMap and publishes it to Kafka.")
public class AirQualityController {

    private final AirQualityService airQualityService;
    private final KafkaProducerService kafkaProducerService;

    @Operation(
            summary = "Get air quality data for specific coordinates",
            description = "Fetches air quality data from OpenWeatherMap based on provided latitude and longitude and publishes it to Kafka.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content = @Content(schema = @Schema(implementation = AirQualityResponse.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<AirQualityResponse> getAirQuality(
            @Parameter(description = "Latitude", required = true) @RequestParam("lat") @NotNull Double lat,
            @Parameter(description = "Longitude", required = true) @RequestParam("lon") @NotNull Double lon) {

        AirQualityResponse response = airQualityService.getAirQuality(lat, lon);
        kafkaProducerService.sendAirQualityData(response);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Submit air quality data manually",
            description = "Accepts manually provided air quality data and sends it to Kafka.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Air quality data payload",
                    content = @Content(schema = @Schema(implementation = AirDataRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Data successfully received and published to Kafka"
                    )
            }
    )
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
