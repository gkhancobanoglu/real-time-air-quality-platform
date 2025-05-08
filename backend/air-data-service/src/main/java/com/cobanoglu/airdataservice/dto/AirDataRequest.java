package com.cobanoglu.airdataservice.dto;

import lombok.Data;

@Data
public class AirDataRequest {
    private Double latitude;
    private Double longitude;
    private Double co;
    private Double no;
    private Double no2;
    private Double o3;
    private Double so2;
    private Double pm2_5;
    private Double pm10;
    private Double nh3;
}