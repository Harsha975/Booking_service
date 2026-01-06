package com.booking_service.service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "seats")
@IdClass(SeatId.class)
public class Seat {
    @Id
    @Column(name = "seat_id")
    private Long seatId;

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "user_id")
    private Long iserId;

    @Column(name = "status" , nullable = false)
    private String status;  //OPEN, LOCKED, BOOKED

    @Column(name = "locked_at" , nullable = false)
    private LocalDateTime lockedAt;

    @Column(name = "locked_by" ,nullable = false)
    private Long lockedBy;
}
