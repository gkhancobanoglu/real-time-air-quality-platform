package com.cobanoglu.datastorageservice.service;

import com.cobanoglu.datastorageservice.model.AirQualityEntity;
import com.cobanoglu.datastorageservice.repository.AirQualityRepository;
import com.cobanoglu.datastorageservice.service.impl.AirQualityStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AirQualityServiceImplTest {

    @Mock
    private AirQualityRepository repository;

    @InjectMocks
    private AirQualityStorageServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        AirQualityEntity entity = new AirQualityEntity();
        service.save(entity);
        verify(repository, times(1)).save(entity);
    }

    @Test
    void testGetAll() {
        AirQualityEntity entity = new AirQualityEntity();
        when(repository.findAll()).thenReturn(Collections.singletonList(entity));

        List<AirQualityEntity> result = service.getAll();

        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAveragePollutionInRegion() {
        AirQualityEntity e1 = new AirQualityEntity();
        e1.setAqi(50.0);
        e1.setPm25(10.0);
        e1.setPm10(20.0);
        e1.setCo(0.5);
        e1.setNo2(5.0);
        e1.setSo2(2.0);
        e1.setO3(8.0);
        e1.setNh3(1.0);

        AirQualityEntity e2 = new AirQualityEntity();
        e2.setAqi(100.0);
        e2.setPm25(20.0);
        e2.setPm10(40.0);
        e2.setCo(1.0);
        e2.setNo2(10.0);
        e2.setSo2(4.0);
        e2.setO3(16.0);
        e2.setNh3(2.0);

        when(repository.findByLatLonRange(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Arrays.asList(e1, e2));

        Map<String, Double> result = service.getAveragePollutionInRegion(0, 100, 0, 100);

        assertEquals(75.0, result.get("avgAqi"));
        assertEquals(15.0, result.get("avgPm25"));
        assertEquals(30.0, result.get("avgPm10"));
        assertEquals(0.75, result.get("avgCo"));
        assertEquals(7.5, result.get("avgNo2"));
        assertEquals(3.0, result.get("avgSo2"));
        assertEquals(12.0, result.get("avgO3"));
        assertEquals(1.5, result.get("avgNh3"));

        verify(repository, times(1)).findByLatLonRange(0, 100, 0, 100);
    }
}
