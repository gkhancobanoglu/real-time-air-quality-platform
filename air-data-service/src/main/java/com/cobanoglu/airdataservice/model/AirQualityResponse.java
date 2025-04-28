package com.cobanoglu.airdataservice.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AirQualityResponse {

    @Valid
    @NotNull
    private List<AirData> list;

    @Data
    public static class AirData {
        @Valid
        @NotNull
        private MainData main;

        @Valid
        @NotNull
        private PollutantData components;

        @NotNull
        private Long dt;

        @NotNull
        private Double lat;

        @NotNull
        private Double lon;
    }

    @Data
    public static class MainData {
        @NotNull
        private Integer aqi;
    }
}
