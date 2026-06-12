package com.example.demo.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public record BookingResponseDTO(
        @Schema(example = "code_from_response")
        String bookingCode,

        @Schema(example = "PG0222")
        String flightNo,

        @Schema(example = "2026-06-12T16:27:50")
        LocalDateTime bookingDate,

        @Schema(example = "5000.0")
        Double totalAmount,

        @Schema(example = "BOOKED")
        String status,

        List<PassengerDTO> passengers
) {
}