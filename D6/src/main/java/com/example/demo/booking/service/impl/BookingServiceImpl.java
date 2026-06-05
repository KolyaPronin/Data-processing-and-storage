package com.example.demo.booking.service.impl;

import com.example.demo.booking.dto.BookingRequestDTO;
import com.example.demo.booking.dto.BookingResponseDTO;
import com.example.demo.booking.dto.PassengerDTO;
import com.example.demo.booking.repository.BookingRepository;
import com.example.demo.booking.repository.PassengerRepository;
import com.example.demo.booking.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final Random rnd = new Random();

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        String bookingCode = generateBookingCode();
        OffsetDateTime now = OffsetDateTime.now();
        double pricePerTicket = 5000.0;
        double totalAmount = request.passengers().size() * pricePerTicket;

        // 1. Вставляем только те 3 поля, которые реально существуют в таблице bookings.bookings
        bookingRepository.insertBooking(bookingCode, now, BigDecimal.valueOf(totalAmount));

        // 2. Для каждого пассажира вставляем билет
        request.passengers().forEach(dto -> {
            String ticketNo = generateTicketNumber();
            String fullName = (dto.firstName() + " " + dto.lastName()).toUpperCase();

            passengerRepository.saveTicket(ticketNo, bookingCode, dto.documentNumber(), fullName);
            bookingRepository.linkTicketToFlight(ticketNo, request.flightNo(), "Economy", pricePerTicket);
        });

        // 3. Возвращаем ответ
        return new BookingResponseDTO(
                bookingCode,
                request.flightNo(),
                now.toLocalDateTime(),
                totalAmount,
                "BOOKED",
                request.passengers()
        );
    }

    @Override
    public BookingResponseDTO getBookingByCode(String bookingCode) {
        var booking = bookingRepository.findById(bookingCode)
                .orElseThrow(() -> new RuntimeException("Бронь с кодом " + bookingCode + " не найдена"));

        String flightNo = bookingRepository.findFlightNoByBookRef(bookingCode);

        List<PassengerDTO> passengers = passengerRepository.findByBookingCode(bookingCode).stream()
                .map(p -> new PassengerDTO(p.getFirstName(), p.getLastName(), p.getDocumentNumber()))
                .collect(Collectors.toList());

        return new BookingResponseDTO(
                booking.getBookingCode(),
                flightNo != null ? flightNo : "UNKNOWN",
                booking.getBookingDate().toLocalDateTime(),
                booking.getTotalAmount().doubleValue(),
                "BOOKED",
                passengers
        );
    }

    private String generateBookingCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        while (code.length() < 6) {
            code.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return code.toString();
    }

    private String generateTicketNumber() {
        StringBuilder sb = new StringBuilder("000543");
        while (sb.length() < 13) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }
}