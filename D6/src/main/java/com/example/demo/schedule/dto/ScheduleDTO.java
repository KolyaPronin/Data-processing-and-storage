package com.example.demo.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ScheduleDTO (
        @Schema(example = "PG0222")
        String flightNo,

        @Schema(example = "SVO")
        String originIata,

        @Schema(example = "AER")
        String destinationIata,

        @Schema(example = "Шереметьево")
        String originName,

        @Schema(example = "Сочи")
        String destinationName,

        @Schema(example = "2026-06-12T19:30:00")
        String arrivalTime,

        @Schema(example = "2026-06-12T16:20:00")
        String departureTime,

        @Schema(example = "Aeroflot")
        String operatedBy
){}