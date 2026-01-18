package com.booking_service.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name ="bookings")
@Data
public class Booking {
    @Id
    @Column(name = "booking_id")
    private UUID bookingId;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "status", nullable = false)
    private BookingStatus status;
    // CONFIRMED, FAILED

    public Booking() {}

    public Booking(UUID bookingId, Long eventId, Long userId, BookingStatus status) {
        this.bookingId = bookingId;
        this.eventId = eventId;
        this.userId = userId;
        this.status = status;
    }
}
