package com.cobanoglu.airdataservice.service;

import com.cobanoglu.airdataservice.model.AirQualityResponse;

public interface AirQualityService {
    AirQualityResponse getAirQuality(double lat, double lon);
}
