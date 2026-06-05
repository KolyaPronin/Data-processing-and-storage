package com.example.demo.booking.dto;

import java.util.List;

public record BookingRequestDTO(
        String flightNo,
        List<PassengerDTO> passengers
) {
}
