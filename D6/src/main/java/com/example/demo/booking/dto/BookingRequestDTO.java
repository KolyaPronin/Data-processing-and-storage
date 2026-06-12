package com.example.demo.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record BookingRequestDTO(
        @Schema(example = "PG0222")
        String flightNo,

        List<PassengerDTO> passengers
) {
}