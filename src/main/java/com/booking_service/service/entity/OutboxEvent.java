package com.booking_service.service.entity;
//
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;
//
//@Entity
//@Table(
//        name = "outbox_event",
//        indexes = {
//                @Index(name = "idx_outbox_published", columnList = "published, created_at")
//        }
//)
//@Data
//public class OutboxEvent {
//
//    @Id
//    private UUID id;
//
//    @Column(nullable = false, updatable = false)
//    private String aggregateType;   // BOOKING
//
//    @Column(nullable = false, updatable = false)
//    private UUID aggregateId;        // bookingId
//
//    @Column(nullable = false, updatable = false)
//    private String eventType;        // PAYMENT_REQUESTED
//
//    @JdbcTypeCode(SqlTypes.JSON)
//    @Column(columnDefinition = "jsonb", nullable = false, updatable = false)
//    private String payload;
//
//    @Getter
//    private boolean published;
//
//    private Instant createdAt;
//    private Instant publishedAt;
//
//    private int retryCount;
//
//    public OutboxEvent() {}
//
//    private OutboxEvent(
//            UUID id,
//            String aggregateType,
//            UUID aggregateId,
//            String eventType,
//            String payload
//    ) {
//        this.id = id;
//        this.aggregateType = aggregateType;
//        this.aggregateId = aggregateId;
//        this.eventType = eventType;
//        this.payload = payload;
//        this.published = false;
//        this.retryCount = 0;
//        this.createdAt = Instant.now();
//    }
//

//

//
//    // Mark retry attempt
//    public void markFailedAttempt() {
//        this.retryCount++;
//    }
//}

@Entity
@Table(
        name = "outbox_event",
        indexes = {
                @Index(name = "idx_outbox_published", columnList = "published, created_at")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutboxEvent {
    @Id
    @Column(name = "outbox_event_id", nullable = false)
    private UUID OutboxEventId;

    @Column(nullable = false)
    private UUID bookingId;

    @Column(nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private Long amount;

    @Column(nullable = false, updatable = false)
    private PaymentStatus status;

    private boolean published;

    private Instant createdAt;

    private Instant publishedAt;
    // Factory
    public static OutboxEvent paymentRequested(
            UUID bookingId,
            Long userId,
            Long amount
    ) {
        OutboxEvent e = new OutboxEvent();
        e.OutboxEventId = UUID.randomUUID();
        e.bookingId = bookingId;
        e.userId = userId;
        e.amount = amount;
        e.status = PaymentStatus.PAYMENT_REQUESTED;
        e.published = false;
        e.createdAt = Instant.now();
        return e;
    }

    // Mark success
    public void markPublished() {
        this.published = true;
        this.publishedAt = Instant.now();
    }
}
