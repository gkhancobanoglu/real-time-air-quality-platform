package com.cobanoglu.airdataservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PollutantData {

    @NotNull
    private Double co;

    @NotNull
    private Double no;

    @NotNull
    private Double no2;

    @NotNull
    private Double o3;

    @NotNull
    private Double so2;

    @NotNull
    private Double pm2_5;

    @NotNull
    private Double pm10;

    @NotNull
    private Double nh3;
}
