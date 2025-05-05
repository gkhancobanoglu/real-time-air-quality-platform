package com.cobanoglu.airdataservice.service;

import com.cobanoglu.airdataservice.model.AirQualityResponse;

public interface KafkaProducerService {
    void sendAirQualityData(AirQualityResponse response);
}
