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

    private int aqi;

    private long timestamp;

    @Column(length = 1000)
    private String description;
}
