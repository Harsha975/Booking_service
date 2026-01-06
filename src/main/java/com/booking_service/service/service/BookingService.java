package com.booking_service.service.service;

import com.booking_service.service.entity.Booking;
import com.booking_service.service.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    private final SeatLockService seatLockService;
    private final BookingRepository bookingRepository;

    public BookingService(
            SeatLockService seatLockService,
            BookingRepository bookingRepository
    ) {
        this.seatLockService = seatLockService;
        this.bookingRepository = bookingRepository;
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

            // 2. (Payment would happen externally)

            // 3. Confirm seats
            seatLockService.confirmSeats(eventId, seatIds, userId);

            // 4. Persist booking
            bookingRepository.save(
                    new Booking(
                            bookingId,
                            eventId,
                            userId,
                            "CONFIRMED"
                    )
            );

        } catch (Exception e) {

            // release locks on failure
            seatLockService.releaseSeats(eventId, userId);

            bookingRepository.save(
                    new Booking(
                            bookingId,
                            eventId,
                            userId,
                            "FAILED"
                    )
            );

            throw e;
        }
    }
}