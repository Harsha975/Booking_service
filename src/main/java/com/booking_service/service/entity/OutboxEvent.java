package com.booking_service.service.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.UUID;
@Entity
@Table(
        name = "outbox_event",
        indexes = {
                @Index(name = "idx_outbox_published", columnList = "published, created_at")
        }
)
@Data
public class OutboxEvent {

    @Id
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String aggregateType;   // BOOKING

    @Column(nullable = false, updatable = false)
    private UUID aggregateId;        // bookingId

    @Column(nullable = false, updatable = false)
    private String eventType;        // PAYMENT_REQUESTED

    @Column(columnDefinition = "jsonb", nullable = false, updatable = false)
    @Type(JsonBinaryType.class)
    private String payload;

    @Getter
    private boolean published;

    private Instant createdAt;
    private Instant publishedAt;

    private int retryCount;

    public OutboxEvent() {}

    private OutboxEvent(
            UUID id,
            String aggregateType,
            UUID aggregateId,
            String eventType,
            String payload
    ) {
        this.id = id;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.published = false;
        this.retryCount = 0;
        this.createdAt = Instant.now();
    }

    // Factory
    public static OutboxEvent paymentRequested(
            UUID bookingId,
            Long userId,
            Long amount
    ) {
        String payload = """
            {
              "bookingId": "%s",
              "userId": %d,
              "amount": %d,
              "status": "PAYMENT_REQUESTED"
            }
            """.formatted(bookingId, userId, amount);

        return new OutboxEvent(
                UUID.randomUUID(),
                "BOOKING",
                bookingId,
                "PAYMENT_REQUESTED",
                payload
        );
    }

    // Mark success
    public void markPublished() {
        this.published = true;
        this.publishedAt = Instant.now();
    }

    // Mark retry attempt
    public void markFailedAttempt() {
        this.retryCount++;
    }
}
