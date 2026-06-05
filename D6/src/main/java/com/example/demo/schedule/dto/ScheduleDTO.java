package com.example.demo.schedule.dto;

public record ScheduleDTO (
        String flightNo,
        String originIata,
        String destinationIata,
        String originName,
        String destinationName,
        String arrivalTime,
        String departureTime,
        String operatedBy
){}
