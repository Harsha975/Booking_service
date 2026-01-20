package com.booking_service.service.service;

import com.booking_service.service.entity.OutboxEvent;
import com.booking_service.service.entity.PaymentRequestedEvent;
import com.booking_service.service.repository.OutboxRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OutboxPublisher {

    private final KafkaTemplate<String, PaymentRequestedEvent> kafkaTemplate;
    private final OutboxRepository outboxRepository;

    public OutboxPublisher(
            KafkaTemplate<String, PaymentRequestedEvent> kafkaTemplate,
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

        for (OutboxEvent message : events) {

//            Message<String> message = MessageBuilder
//                    .withPayload(event.getPayload())
//                    .setHeader(KafkaHeaders.TOPIC, "payment-requested")
//                    .setHeader(KafkaHeaders.KEY, event.getBookingId().toString())
//                    .setHeader("paymentStatus", event.getStatus())
//                    .setHeader("eventId", event.getUserId().toString())
//                    .build();
            PaymentRequestedEvent event =
                    new PaymentRequestedEvent(
                            message.getBookingId(),
                            message.getUserId(),
                            message.getAmount(),
                            message.getStatus()
                    );
            try {
                kafkaTemplate.send(
                        "payment-requested",
                        String.valueOf(message.getBookingId()),
                        event).get(); // 🔒 wait for broker ack
                message.markPublished();             // ✅ only after success
            } catch (Exception ex) {
                log.info(ex.getMessage(), ex);
                break; // avoid hammering Kafka
            }
        }
    }
}
