package com.example.demo.geography.dto;

import jakarta.validation.constraints.Size;

public record CityDTO (
    String id,
    @Size(max = 30)
    String name,
    String country,
    String countryCode,
    String timezone
)
{}
