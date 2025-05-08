package com.cobanoglu.anomalydetectionservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "anomalies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Anomaly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double lat;
    private double lon;

    private double aqi;

    private long timestamp;

    @Column(length = 1000)
    private String description;

    private Double pm25;
    private Double pm10;
    private Double o3;
    private Double no2;
    private Double so2;
    private Double co;
    private Double nh3;
}
