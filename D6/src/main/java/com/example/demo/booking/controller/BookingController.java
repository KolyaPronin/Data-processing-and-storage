package com.example.demo.booking.controller;

import com.example.demo.booking.dto.BookingRequestDTO;
import com.example.demo.booking.dto.BookingResponseDTO;
import com.example.demo.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Бронирование")
@RestController
@RequestMapping("bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Создание бронирования")
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO request) {
        BookingResponseDTO response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{bookingCode}")
    @Operation(summary = "Получение брони по коду")
    public ResponseEntity<BookingResponseDTO> getBookingByCode(
            @PathVariable
            @NotBlank
            @Size(min = 6, max = 6)
            @Parameter(description = "Код брони", example = "code_from_response") String bookingCode) {
        BookingResponseDTO response = bookingService.getBookingByCode(bookingCode);
        return ResponseEntity.ok(response);
    }
}