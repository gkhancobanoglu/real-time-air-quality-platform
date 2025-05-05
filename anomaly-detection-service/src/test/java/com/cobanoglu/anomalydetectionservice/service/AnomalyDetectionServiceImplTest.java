package com.cobanoglu.anomalydetectionservice.service;

import com.cobanoglu.anomalydetectionservice.model.AirQualityResponse;
import com.cobanoglu.anomalydetectionservice.model.Anomaly;
import com.cobanoglu.anomalydetectionservice.model.PollutantData;
import com.cobanoglu.anomalydetectionservice.repository.AnomalyRepository;
import com.cobanoglu.anomalydetectionservice.service.impl.AnomalyDetectionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnomalyDetectionServiceImplTest {

    @Mock
    private AnomalyRepository anomalyRepository;

    @InjectMocks
    private AnomalyDetectionServiceImpl anomalyDetectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckForAnomalies_ThresholdExceeded() {
        AirQualityResponse.AirData airData = new AirQualityResponse.AirData();
        AirQualityResponse.MainData mainData = new AirQualityResponse.MainData();
        mainData.setAqi(5.0);
        airData.setMain(mainData);

        PollutantData pollutantData = new PollutantData();
        pollutantData.setPm2_5(50.0);
        airData.setComponents(pollutantData);

        airData.setLat(41.0);
        airData.setLon(29.0);
        airData.setDt(Instant.now().toEpochMilli());

        AirQualityResponse response = new AirQualityResponse();
        response.setList(List.of(airData));

        when(anomalyRepository.findByTimestampBetween(anyLong(), anyLong())).thenReturn(new ArrayList<>());

        anomalyDetectionService.checkForAnomalies(response);

        verify(anomalyRepository, times(1)).save(any(Anomaly.class));
    }

    @Test
    void testCheckForAnomalies_NullResponse() {
        anomalyDetectionService.checkForAnomalies(null);
        verify(anomalyRepository, times(0)).save(any());
    }

    @Test
    void testCalculateStandardDeviation() {
        Anomaly anomaly1 = Anomaly.builder().aqi(5).build();
        Anomaly anomaly2 = Anomaly.builder().aqi(7).build();
        Anomaly anomaly3 = Anomaly.builder().aqi(9).build();

        List<Anomaly> anomalies = List.of(anomaly1, anomaly2, anomaly3);

        double mean = 7.0;
        double stdDev = anomalyDetectionService.calculateStandardDeviation(anomalies, mean);

        assertTrue(stdDev > 0);
    }

    @Test
    void testFindNearbyAnomalies() {
        Anomaly anomaly1 = Anomaly.builder().lat(41.0).lon(29.0).build();
        Anomaly anomaly2 = Anomaly.builder().lat(41.5).lon(29.5).build();

        List<Anomaly> anomalies = List.of(anomaly1, anomaly2);

        List<Anomaly> nearby = anomalyDetectionService.findNearbyAnomalies(anomalies, 41.0, 29.0, 30.0);

        assertEquals(1, nearby.size());
    }

    @Test
    void testGetAllAnomalies() {
        List<Anomaly> anomalies = List.of(new Anomaly());
        when(anomalyRepository.findAll()).thenReturn(anomalies);

        List<Anomaly> result = anomalyDetectionService.getAllAnomalies();

        assertEquals(1, result.size());
    }
}
