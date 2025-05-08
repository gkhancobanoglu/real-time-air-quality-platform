package com.cobanoglu.datastorageservice.service.impl;

import com.cobanoglu.datastorageservice.model.AirQualityEntity;
import com.cobanoglu.datastorageservice.repository.AirQualityRepository;
import com.cobanoglu.datastorageservice.service.AirQualityStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AirQualityStorageServiceImpl implements AirQualityStorageService {

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

        double avgAqi = data.stream()
                .map(AirQualityEntity::getAqi)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

        double avgPm25 = data.stream()
                .map(AirQualityEntity::getPm25)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average().orElse(0);

        double avgPm10 = data.stream()
                .map(AirQualityEntity::getPm10)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average().orElse(0);

        double avgCo = data.stream()
                .map(AirQualityEntity::getCo)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average().orElse(0);

        double avgNo2 = data.stream()
                .map(AirQualityEntity::getNo2)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average().orElse(0);

        double avgSo2 = data.stream()
                .map(AirQualityEntity::getSo2)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average().orElse(0);

        double avgO3 = data.stream()
                .map(AirQualityEntity::getO3)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average().orElse(0);

        double avgNh3 = data.stream()
                .map(AirQualityEntity::getNh3)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average().orElse(0);

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
