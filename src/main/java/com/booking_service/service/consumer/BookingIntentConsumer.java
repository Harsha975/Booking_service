package com.booking_service.service.consumer;

import com.booking_service.service.service.BookingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class BookingIntentConsumer {

    private final BookingService bookingService;

    public BookingIntentConsumer(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @KafkaListener(
            topics = "booking-intents",
            groupId = "booking-worker-group"
    )
    public void consume(BookingIntentMessage message, Acknowledgment acknowledgment){
        bookingService.processBooking(message.getBookingId(), message.getEventId(), message.getUserId(), message.getSeatIds());
        acknowledgment.acknowledge();
    }
}
