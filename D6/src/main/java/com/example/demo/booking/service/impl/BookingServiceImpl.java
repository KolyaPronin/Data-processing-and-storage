package com.example.demo.booking.service.impl;

import com.example.demo.booking.dto.BookingRequestDTO;
import com.example.demo.booking.dto.BookingResponseDTO;
import com.example.demo.booking.dto.PassengerDTO;
import com.example.demo.booking.repository.BookingRepository;
import com.example.demo.booking.repository.PassengerRepository;
import com.example.demo.booking.service.BookingService;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
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
        if (request.passengers() == null || request.passengers().isEmpty()) {
            throw new BusinessException("Список пассажиров для бронирования не может быть пустым");
        }

        String bookingCode = generateBookingCode();
        OffsetDateTime now = OffsetDateTime.now();
        double pricePerTicket = 5000.0;
        double totalAmount = request.passengers().size() * pricePerTicket;

        bookingRepository.insertBooking(bookingCode, now, BigDecimal.valueOf(totalAmount));

        request.passengers().forEach(dto -> {
            if (dto.firstName() == null || dto.firstName().isBlank() || dto.lastName() == null || dto.lastName().isBlank()) {
                throw new BusinessException("Имя и фамилия пассажира должны быть заполнены");
            }
            if (dto.documentNumber() == null || dto.documentNumber().isBlank()) {
                throw new BusinessException("Номер документа пассажира должен быть заполнен");
            }

            String ticketNo = generateTicketNumber();
            String fullName = (dto.firstName() + " " + dto.lastName()).toUpperCase();

            passengerRepository.saveTicket(ticketNo, bookingCode, dto.documentNumber(), fullName);
            bookingRepository.linkTicketToFlight(ticketNo, request.flightNo(), "Economy", pricePerTicket);
        });

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
                .orElseThrow(() -> new ResourceNotFoundException("Бронь с кодом " + bookingCode + " не найдена"));

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