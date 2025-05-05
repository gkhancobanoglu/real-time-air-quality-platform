package com.cobanoglu.airdataservice.dto;

import lombok.Data;

@Data
public class AirDataRequest {
    private double latitude;
    private double longitude;
    private String parameter;
    private double value;
}
