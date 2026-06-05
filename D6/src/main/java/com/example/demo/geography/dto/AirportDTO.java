package com.example.demo.geography.dto;

public record AirportDTO(
        String iata,
        String name,
        String cityId,
        String cityName,
        String countryCode,
        Double lat,
        Double lon,
        String timezone
) {
}
