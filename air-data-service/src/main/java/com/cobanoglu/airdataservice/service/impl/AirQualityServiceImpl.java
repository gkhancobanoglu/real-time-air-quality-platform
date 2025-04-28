package com.cobanoglu.airdataservice.service.impl;

import com.cobanoglu.airdataservice.exception.AirQualityFetchException;
import com.cobanoglu.airdataservice.model.AirQualityResponse;
import com.cobanoglu.airdataservice.service.AirQualityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.cobanoglu.airdataservice.config.OpenWeatherMapProperties;

@Service
@RequiredArgsConstructor
public class AirQualityServiceImpl implements AirQualityService {

    private final RestTemplate restTemplate;
    private final OpenWeatherMapProperties properties;

    @Override
    public AirQualityResponse getAirQuality(double lat, double lon) {
        try {
            String url = buildUrl(lat, lon);
            return restTemplate.getForObject(url, AirQualityResponse.class);
        } catch (RestClientException e) {
            throw new AirQualityFetchException("Failed to fetch air quality data from OpenWeatherMap", e);
        }
    }

    private String buildUrl(double lat, double lon) {
        return String.format("%s?lat=%f&lon=%f&appid=%s",
                properties.getUrl(), lat, lon, properties.getKey());
    }
}
