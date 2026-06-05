package com.example.demo.route.dto;

import java.time.LocalDate;

public record RouteResponseDTO (
    String id,
    String flightNo,
    String origin,
    String destination,
    LocalDate departureDate,
    Integer availableSeats,
    Double price
){}
