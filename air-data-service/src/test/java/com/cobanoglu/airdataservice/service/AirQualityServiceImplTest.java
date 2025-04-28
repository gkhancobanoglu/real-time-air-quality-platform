package com.cobanoglu.airdataservice.service;

import com.cobanoglu.airdataservice.config.OpenWeatherMapProperties;
import com.cobanoglu.airdataservice.exception.AirQualityFetchException;
import com.cobanoglu.airdataservice.model.AirQualityResponse;
import com.cobanoglu.airdataservice.service.impl.AirQualityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AirQualityServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OpenWeatherMapProperties properties;

    @InjectMocks
    private AirQualityServiceImpl airQualityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(properties.getUrl()).thenReturn("http://dummy-url");
        when(properties.getKey()).thenReturn("dummy-key");
    }

    @Test
    void testGetAirQualitySuccess() {
        AirQualityResponse mockResponse = new AirQualityResponse();
        when(restTemplate.getForObject(anyString(), eq(AirQualityResponse.class))).thenReturn(mockResponse);

        AirQualityResponse response = airQualityService.getAirQuality(10.0, 20.0);

        assertNotNull(response);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(AirQualityResponse.class));
    }

    @Test
    void testGetAirQualityFailure() {
        when(restTemplate.getForObject(anyString(), eq(AirQualityResponse.class))).thenThrow(new RestClientException("API error"));

        assertThrows(AirQualityFetchException.class, () -> airQualityService.getAirQuality(10.0, 20.0));
        verify(restTemplate, times(1)).getForObject(anyString(), eq(AirQualityResponse.class));
    }

    @Test
    void testBuildUrlCorrectly() {
        double lat = 10.0;
        double lon = 20.0;
        String expectedUrl = "http://dummy-url?lat=10.000000&lon=20.000000&appid=dummy-key";

        String actualUrl = (String) ReflectionTestUtils.invokeMethod(airQualityService, "buildUrl", lat, lon);

        assertEquals(expectedUrl, actualUrl);
    }
}
