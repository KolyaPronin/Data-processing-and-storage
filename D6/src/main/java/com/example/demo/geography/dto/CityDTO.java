package com.example.demo.geography.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record CityDTO (
        @Schema(example = "MOW")
        String id,

        @Size(max = 30)
        @Schema(example = "Москва")
        String name,

        @Schema(example = "Россия")
        String country,

        @Schema(example = "RU")
        String countryCode,

        @Schema(example = "Europe/Moscow")
        String timezone
)
{}