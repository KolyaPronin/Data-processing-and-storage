package com.example.demo.checkin.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CheckInRequestDTO(
        @Schema(example = "code_from_response")
        String bookingCode,

        @Schema(example = "1234 567890")
        String documentNumber,

        @Schema(example = "1")
        String seatNumber
) {
}