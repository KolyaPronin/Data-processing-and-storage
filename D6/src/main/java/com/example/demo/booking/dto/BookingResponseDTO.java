package com.example.demo.booking.dto;

import java.time.LocalDateTime;
import java.util.List;

public record BookingResponseDTO(
        String bookingCode,
        String flightNo,
        LocalDateTime bookingDate,
        Double totalAmount,
        String status,
        List<PassengerDTO> passengers
) {
}
