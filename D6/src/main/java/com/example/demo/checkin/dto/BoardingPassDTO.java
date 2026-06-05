package com.example.demo.checkin.dto;

import java.time.LocalDateTime;

public record BoardingPassDTO(
        String fullName,
        String flightNo,
        String ticketNumber,
        String seatNumber,
        String gate,
        LocalDateTime boardingTime
) {
}
