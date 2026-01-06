package com.booking_service.service.consumer;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BookingIntentMessage {

    private UUID bookingId;
    private Long eventId;
    private Long userId;
    private List<Long> seatIds;

    // getters & setters
}

