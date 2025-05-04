package com.cobanoglu.datastorageservice.service;

import com.cobanoglu.datastorageservice.model.AirQualityEntity;

import java.util.List;
import java.util.Map;

public interface AirQualityService {
    void save(AirQualityEntity entity);
    List<AirQualityEntity> getAll();
    Map<String, Double> getAveragePollutionInRegion(double minLat, double maxLat, double minLon, double maxLon);
}
