package com.cobanoglu.anomalydetectionservice.repository;

import com.cobanoglu.anomalydetectionservice.model.Anomaly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly, Long> {

    List<Anomaly> findByTimestampBetween(Long startTimestamp, Long endTimestamp);
}
