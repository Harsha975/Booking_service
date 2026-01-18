package com.booking_service.service.service;

import com.booking_service.service.entity.OutboxEvent;
import com.booking_service.service.repository.OutboxRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxRepository outboxRepository;

    public OutboxPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            OutboxRepository outboxRepository
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.outboxRepository = outboxRepository;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void publish() {

        List<OutboxEvent> events =
                outboxRepository.findTop50ByPublishedFalseOrderByCreatedAt();

        for (OutboxEvent event : events) {

            Message<String> message = MessageBuilder
                    .withPayload(event.getPayload())
                    .setHeader(KafkaHeaders.TOPIC, "payment-requested")
                    .setHeader(KafkaHeaders.KEY, event.getAggregateId().toString())
                    .setHeader("eventType", event.getEventType())
                    .setHeader("aggregateType", event.getAggregateType())
                    .setHeader("eventId", event.getId().toString())
                    .build();

            try {
                kafkaTemplate.send(message).get(); // 🔒 wait for broker ack
                event.markPublished();             // ✅ only after success
            } catch (Exception ex) {
                // ❌ do NOT mark published
                // Kafka retry will happen next scheduler run
                // optional: log + metrics
                break; // avoid hammering Kafka
            }
        }
    }
}
