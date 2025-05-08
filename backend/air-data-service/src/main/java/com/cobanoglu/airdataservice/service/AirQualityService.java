package com.cobanoglu.airdataservice.service;

import com.cobanoglu.airdataservice.model.AirQualityResponse;
import com.cobanoglu.airdataservice.model.PollutantData;

public interface AirQualityService {
    AirQualityResponse getAirQuality(double lat, double lon);
    double calculateAQI(PollutantData data);
}
