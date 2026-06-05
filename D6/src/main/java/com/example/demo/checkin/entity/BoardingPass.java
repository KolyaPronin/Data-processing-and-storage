package com.example.demo.checkin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "boarding_passes", schema = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardingPass {

    @Id
    @Column(name = "ticket_no", length = 13, columnDefinition = "bpchar")
    private String ticketNo;

    @Column(name = "flight_id", nullable = false)
    private Integer flightId;

    @Column(name = "boarding_no", nullable = false)
    private Integer boardingNo;

    @Column(name = "seat_no", length = 4, columnDefinition = "bpchar", nullable = false)
    private String seatNumber;

    @Transient
    private String gate;

    @Transient
    private OffsetDateTime boardingTime;
}