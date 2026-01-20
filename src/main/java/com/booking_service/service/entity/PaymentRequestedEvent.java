package com.booking_service.service.entity;

import java.util.UUID;

public record PaymentRequestedEvent(
        UUID bookingId,
        Long userId,
        Long amount,
        PaymentStatus status
) {}