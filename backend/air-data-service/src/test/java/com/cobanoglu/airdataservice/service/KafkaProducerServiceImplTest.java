package com.cobanoglu.airdataservice.service;

import com.cobanoglu.airdataservice.exception.KafkaPublishException;
import com.cobanoglu.airdataservice.model.AirQualityResponse;
import com.cobanoglu.airdataservice.service.impl.KafkaProducerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class KafkaProducerServiceImplTest {

    @Mock
    private KafkaTemplate<String, AirQualityResponse> kafkaTemplate;

    @InjectMocks
    private KafkaProducerServiceImpl kafkaProducerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kafkaProducerService = new KafkaProducerServiceImpl(kafkaTemplate);
        org.springframework.test.util.ReflectionTestUtils.setField(kafkaProducerService, "topic", "test-topic");
    }

    @Test
    void testSendAirQualityDataSuccess() {
        AirQualityResponse response = new AirQualityResponse();
        when(kafkaTemplate.send(anyString(), any(AirQualityResponse.class))).thenReturn(null); // KafkaTemplate send async olduğu için null dönebilir

        kafkaProducerService.sendAirQualityData(response);

        verify(kafkaTemplate, times(1)).send("test-topic", response);
    }

    @Test
    void testSendAirQualityDataFailure() {
        AirQualityResponse response = new AirQualityResponse();
        doThrow(new RuntimeException("Kafka error")).when(kafkaTemplate).send(anyString(), any(AirQualityResponse.class));

        assertThrows(KafkaPublishException.class, () -> kafkaProducerService.sendAirQualityData(response));
        verify(kafkaTemplate, times(1)).send(anyString(), any(AirQualityResponse.class));
    }
}
