package com.cobanoglu.datastorageservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "air_quality_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirQualityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double lat;
    private Double lon;

    private Integer aqi;

    private Double co;
    private Double no;
    private Double no2;
    private Double o3;
    private Double so2;
    private Double pm25;
    private Double pm10;
    private Double nh3;

    private Long timestamp;
}
