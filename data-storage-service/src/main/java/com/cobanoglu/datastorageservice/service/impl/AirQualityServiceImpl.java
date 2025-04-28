package com.cobanoglu.datastorageservice.service.impl;

import com.cobanoglu.datastorageservice.model.AirQualityEntity;
import com.cobanoglu.datastorageservice.repository.AirQualityRepository;
import com.cobanoglu.datastorageservice.service.AirQualityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirQualityServiceImpl implements AirQualityService {

    private final AirQualityRepository repository;

    @Override
    public void save(AirQualityEntity entity) {
        repository.save(entity);
    }

    @Override
    public List<AirQualityEntity> getAll() {
        return repository.findAll();
    }
}
