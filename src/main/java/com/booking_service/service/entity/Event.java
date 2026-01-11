package com.booking_service.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @Column(name = "event_Id")
    private Long eventId;

    @Column(name = "movie_name" , nullable = false)
    private String movieName;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime start_time;

    @Column(name = "end_time" , nullable = false)
    private  LocalDateTime end_time;

    @Column(name = "city" , nullable = false)
    private String city;
}
