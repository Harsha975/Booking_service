package com.booking_service.service.scheduler;

import com.booking_service.service.repository.SeatRepository;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

public class SeatUnlockScheduler {
    private final SeatRepository seatRepository;

    public SeatUnlockScheduler(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Scheduled(fixedDelay = 60_000) // every 1 minute
    public void unlockExpiredSeats() {

        LocalDateTime expiryTime =
                LocalDateTime.now().minusMinutes(10);

        int unlocked = seatRepository.unlockExpiredSeats(expiryTime);

        if (unlocked > 0) {
            System.out.println("Unlocked seats: " + unlocked);
        }
    }
}
