package com.example.demo.route.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "flights", schema = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightInstance {

    @Id
    @Column(name = "flight_id")
    private Integer id;

    @Column(name = "flight_no", nullable = false)
    private String flightNo;

    @Column(name = "departure_airport", length = 3, nullable = false)
    private String origin;

    @Column(name = "arrival_airport", length = 3, nullable = false)
    private String destination;

    @Column(name = "scheduled_departure", nullable = false)
    private OffsetDateTime departureDate;

    @Transient
    private Integer availableSeats;

    @Transient
    private Double price;
}