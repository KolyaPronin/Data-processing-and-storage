package com.example.demo.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tickets", schema = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {

    @Id
    @Column(name = "ticket_no", length = 13, columnDefinition = "bpchar")
    private String ticketNo;

    @Column(name = "book_ref", nullable = false, length = 6, columnDefinition = "bpchar")
    private String bookingCode;

    @Column(name = "passenger_id", nullable = false)
    private String documentNumber;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    public String getFirstName() {
        if (passengerName != null && passengerName.contains(" ")) {
            return passengerName.split(" ")[0];
        }
        return passengerName;
    }

    public String getLastName() {
        if (passengerName != null && passengerName.contains(" ")) {
            String[] parts = passengerName.split(" ");
            return parts.length > 1 ? parts[1] : "";
        }
        return "";
    }
}