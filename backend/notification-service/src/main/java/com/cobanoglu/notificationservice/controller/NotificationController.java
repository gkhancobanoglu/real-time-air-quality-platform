package com.cobanoglu.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@Slf4j
@Tag(name = "Notification Service", description = "Provides real-time anomaly updates via Server-Sent Events (SSE)")
public class NotificationController {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8082")
            .build();

    @Operation(
            summary = "Stream latest anomalies",
            description = "Streams the latest anomaly data every 2 seconds using Server-Sent Events (SSE)."
    )
    @GetMapping(path = "/api/notifications/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamNotifications() {
        log.info("SSE baglantisi kuruldu, anomaly-service'den veri alinacak");

        return Flux.interval(Duration.ofSeconds(2))
                .flatMap(tick -> fetchLatestAnomaly())
                .onErrorResume(e -> {
                    log.error("anomaly-service istegi basarisiz: {}", e.getMessage());
                    return Mono.empty();
                });
    }

    private Mono<String> fetchLatestAnomaly() {
        return webClient.get()
                .uri("/api/anomalies/latest")
                .retrieve()
                .bodyToMono(String.class);
    }
}
