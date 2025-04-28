package com.cobanoglu.datastorageservice.repository;

import com.cobanoglu.datastorageservice.model.AirQualityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirQualityRepository extends JpaRepository<AirQualityEntity, Long> {
}