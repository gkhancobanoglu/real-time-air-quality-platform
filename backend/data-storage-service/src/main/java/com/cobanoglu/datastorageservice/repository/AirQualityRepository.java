package com.cobanoglu.datastorageservice.repository;

import com.cobanoglu.datastorageservice.model.AirQualityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirQualityRepository extends JpaRepository<AirQualityEntity, Long> {

    @Query("SELECT e FROM AirQualityEntity e WHERE e.lat BETWEEN :minLat AND :maxLat AND e.lon BETWEEN :minLon AND :maxLon")
    List<AirQualityEntity> findByLatLonRange(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLon") double minLon,
            @Param("maxLon") double maxLon
    );

}