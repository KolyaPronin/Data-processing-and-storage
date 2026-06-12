package com.example.demo.geography.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AirportDTO(
        @Schema(example = "SVO")
        String iata,

        @Schema(example = "Шереметьево имени А.С. Пушкина")
        String name,

        @Schema(example = "MOW")
        String cityId,

        @Schema(example = "Москва")
        String cityName,

        @Schema(example = "RU")
        String countryCode,

        @Schema(example = "55.972642")
        Double lat,

        @Schema(example = "37.414589")
        Double lon,

        @Schema(example = "Europe/Moscow")
        String timezone
) {
}