package com.cobanoglu.anomalydetectionservice.service;

import com.cobanoglu.anomalydetectionservice.exception.AnomalyNotFoundException;
import com.cobanoglu.anomalydetectionservice.exception.InvalidAirDataException;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.*;

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
        ReflectionTestUtils.setField(anomalyDetectionService, "aqiThreshold", 3);
    }

    @Test
    void testCheckForAnomalies_ThresholdExceeded() {
        AirQualityResponse.AirData airData = new AirQualityResponse.AirData();
        airData.setMain(new AirQualityResponse.MainData() {{ setAqi(5.0); }});
        airData.setComponents(new PollutantData() {{ setPm2_5(50.0); }});
        airData.setLat(41.0);
        airData.setLon(29.0);
        airData.setDt(Instant.now().toEpochMilli());

        AirQualityResponse response = new AirQualityResponse();
        response.setList(Collections.singletonList(airData));

        when(anomalyRepository.findByTimestampBetween(anyLong(), anyLong())).thenReturn(Collections.emptyList());

        anomalyDetectionService.checkForAnomalies(response);
        verify(anomalyRepository, times(1)).save(any(Anomaly.class));
    }

    @Test
    void testCheckForAnomalies_NullResponse() {
        assertThrows(InvalidAirDataException.class, () -> anomalyDetectionService.checkForAnomalies(null));
    }

    @Test
    void testCheckForAnomalies_NullList() {
        AirQualityResponse response = new AirQualityResponse();
        response.setList(null);
        assertThrows(InvalidAirDataException.class, () -> anomalyDetectionService.checkForAnomalies(response));
    }

    @Test
    void testCheckForAnomalies_NullMainOrComponents() {
        AirQualityResponse.AirData airData = new AirQualityResponse.AirData();
        airData.setMain(null);
        AirQualityResponse response = new AirQualityResponse();
        response.setList(Collections.singletonList(airData));

        anomalyDetectionService.checkForAnomalies(response);
        verify(anomalyRepository, never()).save(any());
    }

    @Test
    void testCheckForAnomalies_SaveFails() {
        AirQualityResponse.AirData airData = new AirQualityResponse.AirData();
        airData.setMain(new AirQualityResponse.MainData() {{ setAqi(10.0); }});
        airData.setComponents(new PollutantData() {{ setPm2_5(100.0); }});
        airData.setLat(41.0);
        airData.setLon(29.0);
        airData.setDt(Instant.now().toEpochMilli());

        AirQualityResponse response = new AirQualityResponse();
        response.setList(Collections.singletonList(airData));

        when(anomalyRepository.findByTimestampBetween(anyLong(), anyLong())).thenReturn(Collections.emptyList());
        doThrow(new RuntimeException("db fail")).when(anomalyRepository).save(any());

        assertThrows(RuntimeException.class, () -> anomalyDetectionService.checkForAnomalies(response));
    }

    @Test
    void testCalculateStandardDeviation() {
        List<Anomaly> list = Arrays.asList(
                Anomaly.builder().aqi(5).build(),
                Anomaly.builder().aqi(7).build(),
                Anomaly.builder().aqi(9).build()
        );
        double stdDev = anomalyDetectionService.calculateStandardDeviation(list, 7);
        assertTrue(stdDev > 0);
    }

    @Test
    void testFindNearbyAnomalies() {
        List<Anomaly> list = Arrays.asList(
                Anomaly.builder().lat(41.0).lon(29.0).build(),
                Anomaly.builder().lat(50.0).lon(40.0).build()
        );
        List<Anomaly> result = anomalyDetectionService.findNearbyAnomalies(list, 41.0, 29.0, 100);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllAnomalies() {
        when(anomalyRepository.findAll()).thenReturn(Collections.singletonList(new Anomaly()));
        assertEquals(1, anomalyDetectionService.getAllAnomalies().size());
    }

    @Test
    void testGetAnomaliesBetween() {
        when(anomalyRepository.findByTimestampBetween(anyLong(), anyLong())).thenReturn(Collections.singletonList(new Anomaly()));
        assertEquals(1, anomalyDetectionService.getAnomaliesBetween(1, 2).size());
    }

    @Test
    void testGetAnomalyById_Found() {
        Anomaly anomaly = new Anomaly();
        when(anomalyRepository.findById(1L)).thenReturn(Optional.of(anomaly));
        assertEquals(anomaly, anomalyDetectionService.getAnomalyById(1L));
    }

    @Test
    void testGetAnomalyById_NotFound() {
        when(anomalyRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AnomalyNotFoundException.class, () -> anomalyDetectionService.getAnomalyById(1L));
    }

    @Test
    void testGetLatestAnomaly() {
        Optional<Anomaly> anomaly = Optional.of(new Anomaly());
        when(anomalyRepository.findTopByOrderByTimestampDesc()).thenReturn(anomaly);
        assertTrue(anomalyDetectionService.getLatestAnomaly().isPresent());
    }
}
