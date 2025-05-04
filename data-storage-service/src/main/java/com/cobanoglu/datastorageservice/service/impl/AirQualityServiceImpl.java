package com.cobanoglu.datastorageservice.service.impl;

import com.cobanoglu.datastorageservice.model.AirQualityEntity;
import com.cobanoglu.datastorageservice.repository.AirQualityRepository;
import com.cobanoglu.datastorageservice.service.AirQualityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AirQualityServiceImpl implements AirQualityService {

    private final AirQualityRepository airQualityRepository;

    @Override
    public void save(AirQualityEntity entity) {
        airQualityRepository.save(entity);
    }

    @Override
    public List<AirQualityEntity> getAll() {
        return airQualityRepository.findAll();
    }

    @Override
    public Map<String, Double> getAveragePollutionInRegion(double minLat, double maxLat, double minLon, double maxLon) {
        List<AirQualityEntity> data = airQualityRepository.findByLatLonRange(minLat, maxLat, minLon, maxLon);

        double avgAqi = data.stream().mapToDouble(AirQualityEntity::getAqi).average().orElse(0);
        double avgPm25 = data.stream().mapToDouble(AirQualityEntity::getPm25).average().orElse(0);
        double avgPm10 = data.stream().mapToDouble(AirQualityEntity::getPm10).average().orElse(0);
        double avgCo = data.stream().mapToDouble(AirQualityEntity::getCo).average().orElse(0);
        double avgNo2 = data.stream().mapToDouble(AirQualityEntity::getNo2).average().orElse(0);
        double avgSo2 = data.stream().mapToDouble(AirQualityEntity::getSo2).average().orElse(0);
        double avgO3 = data.stream().mapToDouble(AirQualityEntity::getO3).average().orElse(0);
        double avgNh3 = data.stream().mapToDouble(AirQualityEntity::getNh3).average().orElse(0);

        Map<String, Double> result = new HashMap<>();
        result.put("avgAqi", avgAqi);
        result.put("avgPm25", avgPm25);
        result.put("avgPm10", avgPm10);
        result.put("avgCo", avgCo);
        result.put("avgNo2", avgNo2);
        result.put("avgSo2", avgSo2);
        result.put("avgO3", avgO3);
        result.put("avgNh3", avgNh3);

        return result;
    }
}
