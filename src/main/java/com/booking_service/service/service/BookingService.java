package com.booking_service.service.service;

import com.booking_service.service.entity.Booking;
import com.booking_service.service.entity.BookingStatus;
import com.booking_service.service.entity.OutboxEvent;
import com.booking_service.service.repository.BookingRepository;
import com.booking_service.service.repository.OutboxRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    private final SeatLockService seatLockService;
    private final BookingRepository bookingRepository;
    private final OutboxRepository  outboxRepository;
    public BookingService(
            SeatLockService seatLockService,
            BookingRepository bookingRepository, OutboxRepository outboxRepository
    ) {
        this.seatLockService = seatLockService;
        this.bookingRepository = bookingRepository;
        this.outboxRepository = outboxRepository;
    }

    @Transactional
    public void processBooking(
            UUID bookingId,
            Long eventId,
            Long userId,
            List<Long> seatIds
    ) {
        try {
            // 1. Lock seats
            seatLockService.lockSeats(eventId, seatIds, userId);

            // 2. Update the status
            bookingRepository.save(
                    new Booking(
                            bookingId,
                            eventId,
                            userId,
                            BookingStatus.PENDING_PAYMENT
                    )
            );


            // 3. Write PAYMENT_REQUESTED event to outbox
            OutboxEvent paymentRequested = OutboxEvent.paymentRequested(
                    bookingId,
                    userId,
                    calculateAmount(seatIds)
            );
            outboxRepository.save(paymentRequested);

            // 2. (Payment would happen externally)

//            // 3. Confirm seats
//            seatLockService.confirmSeats(eventId, seatIds, userId);
//
//            // 4. Persist booking
//            bookingRepository.save(
//                    new Booking(
//                            bookingId,
//                            eventId,
//                            userId,
//                            BookingStatus.CONFIRMED
//                    )
//            );

        } catch (Exception e) {

            // release locks on failure
            seatLockService.releaseSeats(eventId, userId);

            bookingRepository.save(
                    new Booking(
                            bookingId,
                            eventId,
                            userId,
                            BookingStatus.FAILED
                    )
            );

            throw e;
        }
    }
    private Long calculateAmount(List<Long> seatIds) {
        return seatIds.size() * 250L; // example pricing
    }
}