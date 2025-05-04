package com.cobanoglu.notificationservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@Slf4j
public class NotificationController {

    private final Sinks.Many<String> sink;

    public NotificationController() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping(path = "/api/notifications/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamNotifications() {
        log.info("🎯 New SSE subscriber connected");
        return sink.asFlux().delayElements(Duration.ofMillis(100));
    }

    public void sendNotification(String message) {
        log.info("📢 Broadcasting: {}", message);
        sink.tryEmitNext(message);
    }
}
