package com.cobanoglu.datastorageservice.consumer;

import com.cobanoglu.datastorageservice.model.AirQualityEntity;
import com.cobanoglu.datastorageservice.model.AirQualityResponse;
import com.cobanoglu.datastorageservice.repository.AirQualityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AirQualityKafkaListener {

    private final AirQualityRepository airQualityRepository;

    @KafkaListener(
        topics = "air-data",
        groupId = "data-storage-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(AirQualityResponse response) {
        if (response != null && response.getList() != null) {
            response.getList().forEach(airData -> {
                AirQualityEntity entity = AirQualityEntity.builder()
                        .aqi(airData.getMain().getAqi())
                        .co(airData.getComponents().getCo())
                        .no(airData.getComponents().getNo())
                        .no2(airData.getComponents().getNo2())
                        .o3(airData.getComponents().getO3())
                        .so2(airData.getComponents().getSo2())
                        .pm25(airData.getComponents().getPm2_5())
                        .pm10(airData.getComponents().getPm10())
                        .nh3(airData.getComponents().getNh3())
                        .lat(airData.getLat())
                        .lon(airData.getLon())
                        .timestamp(airData.getDt())
                        .build();
                airQualityRepository.save(entity);
            });
            log.info(" Air quality data successfully saved to database.");
        } else {
            log.warn(" Received null or empty air quality data from Kafka.");
        }
    }
}
