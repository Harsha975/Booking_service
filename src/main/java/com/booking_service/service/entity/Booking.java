package com.booking_service.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name ="bookings")
public class Booking {
    @Id
    @Column(name = "booking_id")
    private UUID bookingId;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "status", nullable = false)
    private String status;
    // CONFIRMED, FAILED

    public Booking() {}

    public Booking(UUID bookingId, Long eventId, Long userId, String status) {
        this.bookingId = bookingId;
        this.eventId = eventId;
        this.userId = userId;
        this.status = status;
    }
}
