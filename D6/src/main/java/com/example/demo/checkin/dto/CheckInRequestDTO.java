package com.example.demo.checkin.dto;

public record CheckInRequestDTO(
        String bookingCode,
        String documentNumber,
        String seatNumber
) {
}
