package com.example.demo.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PassengerDTO(
        @Schema(example = "IVAN")
        String firstName,

        @Schema(example = "IVANOV")
        String lastName,

        @Schema(example = "1234 567890")
        String documentNumber
) {
}