package com.cobanoglu.datastorageservice.service.impl;

import com.cobanoglu.datastorageservice.model.AirQualityEntity;
import com.cobanoglu.datastorageservice.repository.AirQualityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AirQualityServiceImplTest {

    @Mock
    private AirQualityRepository repository;

    @InjectMocks
    private AirQualityServiceImpl service;

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
}
