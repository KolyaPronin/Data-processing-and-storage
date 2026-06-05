package com.example.demo.booking.service;

import com.example.demo.booking.dto.BookingRequestDTO;
import com.example.demo.booking.dto.BookingResponseDTO;

public interface BookingService {

    BookingResponseDTO createBooking(BookingRequestDTO request);

    BookingResponseDTO getBookingByCode(String bookingCode);
}
