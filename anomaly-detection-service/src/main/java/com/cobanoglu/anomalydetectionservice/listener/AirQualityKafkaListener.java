package com.cobanoglu.anomalydetectionservice.listener;

import com.cobanoglu.anomalydetectionservice.model.AirQualityResponse;
import com.cobanoglu.anomalydetectionservice.service.AnomalyDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AirQualityKafkaListener {

    private final AnomalyDetectionService anomalyDetectionService;

    @KafkaListener(topics = "air-data", groupId = "anomaly-detector", containerFactory = "kafkaListenerContainerFactory")
    public void listen(AirQualityResponse response) {
        log.info("Received air quality data from Kafka: {}", response);
        anomalyDetectionService.checkForAnomalies(response);
    }
}
