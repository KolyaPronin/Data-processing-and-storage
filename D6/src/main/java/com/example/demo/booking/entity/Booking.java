package com.example.demo.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "bookings", schema = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @Column(name = "book_ref", length = 6, columnDefinition = "bpchar")
    private String bookingCode;

    @Column(name = "book_date", nullable = false)
    private OffsetDateTime bookingDate;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Transient
    private String status = "BOOKED";

    @Transient
    private String flightNo;
}