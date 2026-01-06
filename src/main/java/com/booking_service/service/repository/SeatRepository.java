package com.booking_service.service.repository;

import com.booking_service.service.entity.Seat;
import com.booking_service.service.entity.SeatId;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface SeatRepository extends JpaRepository<Seat , SeatId> {

    @Modifying
    @Query("""
        UPDATE Seat s
        Set s.status = 'LOCKED',
            s.lockedAt= :now,
            s.lockedBy= :userId
        WHERE s.seatId = :seatId
        AND s.eventId = :eventId
        AND s.status = 'OPEN'
""")
    int lock(
            @Param("seatId") Long seatId,
            @Param("eventId") Long eventId,
            @Param("userId") Long userId,
            @Param("now")LocalDateTime now
            );


    @Modifying
    @Query("""
        UPDATE Seat s
        Set s.status = "OPEN",
            s.lockedAt= NULL,
            s.lockedBy= NULL
        WHERE s.status = "LOCKED"
        AND s.lockedAt < :expiryTime
""")
    int unlockExpiredSeats(
            @Param("expiryTime") LocalDateTime expiryTime
    );

    @Modifying
    @Query("""
        UPDATE Seat s
        Set s.status = 'BOOKED'
        WHERE s.seatId = :seatId
        AND s.eventId = :eventId
        AND s.status = 'LOCKED'
        AND s.lockedBy = :userId
""")
    int confirmSeat(
            @Param("seatId") Long seatId,
            @Param("eventId") Long eventId,
            @Param("userId") Long userId
    );

    @Modifying
    @Query("""
    UPDATE Seat s
    SET s.status = 'OPEN',
        s.lockedAt = NULL,
        s.lockedBy = NULL
    WHERE s.eventId = :eventId
      AND s.lockedBy = :userId
""")
    int releaseUserLocks(
            @Param("eventId") Long eventId,
            @Param("userId") Long userId
    );

}
