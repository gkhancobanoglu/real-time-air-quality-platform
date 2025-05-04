package com.cobanoglu.notificationservice.listener;

import com.cobanoglu.notificationservice.controller.NotificationController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnomalyKafkaListener {

    private final NotificationController notificationController;

    @KafkaListener(topics = "${kafka.topic.anomaly-data}", groupId = "notification-service")
    public void listen(String message) {
        log.info("✅ Received anomaly from Kafka: {}", message);
        notificationController.sendNotification(message);
    }
}
