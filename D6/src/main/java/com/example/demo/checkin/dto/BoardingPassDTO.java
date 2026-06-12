package com.example.demo.checkin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record BoardingPassDTO(
        @Schema(example = "IVAN IVANOV")
        String fullName,

        @Schema(example = "PG0222")
        String flightNo,

        @Schema(example = "TKT-998822")
        String ticketNumber,

        String seatNumber,

        String gate,

        LocalDateTime boardingTime
) {
}