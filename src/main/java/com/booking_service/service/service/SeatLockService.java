package com.booking_service.service.service;


import com.booking_service.service.repository.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeatLockService {

    private SeatRepository seatRepository;

    public SeatLockService(SeatRepository seatRepository){
        this.seatRepository = seatRepository;
    }

    /**
     * Attempts to lock all requested seats.
     * Either ALL seats are locked, or NONE.
     */
    @Transactional
    public void lockSeats(
            Long eventId,
            List<Long> seatIds,
            Long userId
    ){
        LocalDateTime now = LocalDateTime.now();

        for(Long seatId : seatIds){
            int updated = seatRepository.lock(seatId,eventId, userId, now);
            if(updated == 0) {
                throw  new IllegalStateException("Seat already locked"+seatId);
            }
        }
    }

    /**
     * Release all locks for this user & event
     */
    @Transactional
    public void releaseSeats(
            Long eventId,
            Long userId
    ) {
        seatRepository.releaseUserLocks(eventId, userId);
    }

    /**
     * Confirm seats after payment success
     */
    @Transactional
    public void confirmSeats(
            Long eventId,
            List<Long> seatIds,
            Long userId
    ) {
        for (Long seatId : seatIds) {
            int updated = seatRepository.confirmSeat(
                    seatId,
                    eventId,
                    userId
            );

            if (updated == 0) {
                throw new IllegalStateException(
                        "Seat confirmation failed: " + seatId
                );
            }
        }
    }
}
