package com.booking_service.service.entity;

import java.io.Serializable;
import java.util.Objects;

public class SeatId implements Serializable {
    private Long seatId;
    private Long eventId;


    public SeatId() {}

    public SeatId(Long seatId, Long eventId) {
        this.seatId = seatId;
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof SeatId seatId1)) return false;
        return Objects.equals(seatId , seatId1.seatId ) && Objects.equals(eventId, seatId1.eventId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(seatId, eventId);
    }
}
