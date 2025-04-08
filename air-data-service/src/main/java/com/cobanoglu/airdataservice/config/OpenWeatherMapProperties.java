package com.cobanoglu.airdataservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "openweathermap.api")
@Component
@Getter
@Setter
public class OpenWeatherMapProperties {
    private String url;
    private String key;
}
