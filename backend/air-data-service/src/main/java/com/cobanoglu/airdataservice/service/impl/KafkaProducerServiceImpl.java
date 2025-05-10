package com.cobanoglu.airdataservice.service.impl;

import com.cobanoglu.airdataservice.exception.KafkaPublishException;
import com.cobanoglu.airdataservice.model.AirQualityResponse;
import com.cobanoglu.airdataservice.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, AirQualityResponse> kafkaTemplate;

    @Value("${kafka.topic.air-data}")
    private String topic;

    @Override
    public void sendAirQualityData(AirQualityResponse response) {
        try {
            kafkaTemplate.send(topic, response);
            log.info(" Sent air quality data to Kafka topic '{}'", topic);
        } catch (Exception e) {
            log.error(" Failed to send data to Kafka topic '{}'", topic, e);
            throw new KafkaPublishException("Failed to send air quality data to Kafka", e);
        }
    }
}
