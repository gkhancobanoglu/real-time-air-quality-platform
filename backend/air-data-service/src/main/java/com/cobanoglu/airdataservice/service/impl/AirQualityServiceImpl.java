package com.cobanoglu.airdataservice.service.impl;

import com.cobanoglu.airdataservice.config.OpenWeatherMapProperties;
import com.cobanoglu.airdataservice.exception.AirQualityFetchException;
import com.cobanoglu.airdataservice.model.AirQualityResponse;
import com.cobanoglu.airdataservice.model.PollutantData;
import com.cobanoglu.airdataservice.service.AirQualityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirQualityServiceImpl implements AirQualityService {

    private final RestTemplate restTemplate;
    private final OpenWeatherMapProperties properties;

    @Override
    public AirQualityResponse getAirQuality(double lat, double lon) {
        try {
            String url = buildUrl(lat, lon);
            AirQualityResponse response = restTemplate.getForObject(url, AirQualityResponse.class);

            if (response == null || response.getList() == null) {
                throw new AirQualityFetchException("Received empty air quality data from OpenWeatherMap");
            }

            response.getList().forEach(data -> {
                data.setLat(lat);
                data.setLon(lon);
            });

            return response;

        } catch (RestClientException e) {
            throw new AirQualityFetchException("Failed to fetch air quality data from OpenWeatherMap", e);
        }
    }

    @Override
    public double calculateAQI(PollutantData data) {
        Map<String, Double> pollutants = new HashMap<>();
        pollutants.put("pm25", data.getPm2_5());
        pollutants.put("pm10", data.getPm10());
        pollutants.put("o3", convertUgPerM3ToPpm(data.getO3(), 48.00)); // O3 ppm
        pollutants.put("no2", data.getNo2());
        pollutants.put("so2", data.getSo2());
        pollutants.put("co", convertUgPerM3ToPpm(data.getCo(), 28.01)); // CO ppm
        pollutants.put("nh3", data.getNh3());

        double maxAQI = 1.0;

        for (Map.Entry<String, Double> entry : pollutants.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();

            if (value == null) continue;

            double aqi = AQICalculator.calculate(key, value);
            maxAQI = Math.max(maxAQI, aqi);
        }

        return Math.round(maxAQI);
    }

    private String buildUrl(double lat, double lon) {
        return String.format("%s?lat=%f&lon=%f&appid=%s", properties.getUrl(), lat, lon, properties.getKey());
    }


    private double convertUgPerM3ToPpm(Double ugm3, double molarMass) {
        if (ugm3 == null) return 0.0;
        return (ugm3 * 0.0409) / molarMass;
    }

    private static class AQICalculator {
        static double calculate(String pollutant, double value) {
            switch (pollutant) {
                case "pm25":
                    return aqiFromRanges(value, new double[][]{
                            {0.0, 12.0, 0, 50},
                            {12.1, 35.4, 51, 100},
                            {35.5, 55.4, 101, 150},
                            {55.5, 150.4, 151, 200},
                            {150.5, 250.4, 201, 300},
                            {250.5, 500.0, 301, 500}
                    });
                case "pm10":
                    return aqiFromRanges(value, new double[][]{
                            {0, 54, 0, 50},
                            {55, 154, 51, 100},
                            {155, 254, 101, 150},
                            {255, 354, 151, 200},
                            {355, 424, 201, 300},
                            {425, 604, 301, 500}
                    });
                case "o3":
                    return aqiFromRanges(value, new double[][]{
                            {0.000, 0.054, 0, 50},
                            {0.055, 0.070, 51, 100},
                            {0.071, 0.085, 101, 150},
                            {0.086, 0.105, 151, 200},
                            {0.106, 0.200, 201, 300},
                            {0.201, 0.604, 301, 500}
                    });
                case "no2":
                    return aqiFromRanges(value, new double[][]{
                            {0, 53, 0, 50},
                            {54, 100, 51, 100},
                            {101, 360, 101, 150},
                            {361, 649, 151, 200},
                            {650, 1249, 201, 300},
                            {1250, 2049, 301, 500}
                    });
                case "so2":
                    return aqiFromRanges(value, new double[][]{
                            {0, 35, 0, 50},
                            {36, 75, 51, 100},
                            {76, 185, 101, 150},
                            {186, 304, 151, 200},
                            {305, 604, 201, 300},
                            {605, 1004, 301, 500}
                    });
                case "co":
                    return aqiFromRanges(value, new double[][]{
                            {0.0, 4.4, 0, 50},
                            {4.5, 9.4, 51, 100},
                            {9.5, 12.4, 101, 150},
                            {12.5, 15.4, 151, 200},
                            {15.5, 30.4, 201, 300},
                            {30.5, 50.4, 301, 500}
                    });
                case "nh3":
                    return aqiFromRanges(value, new double[][]{
                            {0, 200, 0, 100},
                            {201, 400, 101, 200},
                            {401, 800, 201, 300},
                            {801, 1000, 301, 500}
                    });
                default:
                    return 0.0; // daha anlamlı default
            }
        }

        private static double aqiFromRanges(double value, double[][] ranges) {
            log.debug("AQI hesaplanan değer: {}", value);
            for (double[] range : ranges) {
                log.debug("Kontrol: {} - {} | AQI: {} - {}", range[0], range[1], range[2], range[3]);
                if (value >= range[0] && value <= range[1]) {
                    double mapped = map(value, range[0], range[1], range[2], range[3]);
                    log.debug("Eşleşen aralık bulundu, AQI: {}", mapped);
                    return mapped;
                }
            }
            log.debug("Eşleşme bulunamadı, AQI: 500 (varsayılan)");
            return 500;
        }

        private static double map(double value, double inMin, double inMax, double outMin, double outMax) {
            return ((value - inMin) / (inMax - inMin)) * (outMax - outMin) + outMin;
        }
    }
}
