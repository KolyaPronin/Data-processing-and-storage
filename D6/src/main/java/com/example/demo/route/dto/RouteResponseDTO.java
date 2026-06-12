package com.example.demo.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record RouteResponseDTO (
        @Schema(example = "14253")
        String id,

        @Schema(example = "PG0222")
        String flightNo,

        @Schema(example = "SVO")
        String origin,

        @Schema(example = "AER")
        String destination,

        @Schema(example = "2026-06-12")
        LocalDate departureDate,

        @Schema(example = "42")
        Integer availableSeats,

        @Schema(example = "5000.0")
        Double price
){}