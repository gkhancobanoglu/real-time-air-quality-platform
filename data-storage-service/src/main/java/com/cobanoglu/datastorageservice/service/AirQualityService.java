package com.cobanoglu.datastorageservice.service;

import com.cobanoglu.datastorageservice.model.AirQualityEntity;

import java.util.List;

public interface AirQualityService {
    void save(AirQualityEntity entity);
    List<AirQualityEntity> getAll();
}
