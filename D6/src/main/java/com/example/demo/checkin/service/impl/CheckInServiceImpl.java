package com.example.demo.checkin.service.impl;

import com.example.demo.booking.entity.Booking;
import com.example.demo.booking.entity.Passenger;
import com.example.demo.booking.repository.BookingRepository;
import com.example.demo.booking.repository.PassengerRepository;
import com.example.demo.checkin.dto.BoardingPassDTO;
import com.example.demo.checkin.dto.CheckInRequestDTO;
import com.example.demo.checkin.entity.BoardingPass;
import com.example.demo.checkin.repository.BoardingPassRepository;
import com.example.demo.checkin.service.CheckInService;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService {

    private final BoardingPassRepository boardingPassRepository;
    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BoardingPassDTO checkIn(CheckInRequestDTO request) {
        if (request.bookingCode() == null || request.bookingCode().isBlank()) {
            throw new BusinessException("Код бронирования не может быть пустым");
        }
        if (request.documentNumber() == null || request.documentNumber().isBlank()) {
            throw new BusinessException("Номер документа не может быть пустым");
        }
        if (request.seatNumber() == null || request.seatNumber().isBlank()) {
            throw new BusinessException("Номер места не может быть пустым");
        }

        Booking booking = bookingRepository.findById(request.bookingCode())
                .orElseThrow(() -> new ResourceNotFoundException("Бронирование с кодом " + request.bookingCode() + " не найдено"));

        Passenger passenger = passengerRepository.findByBookingCode(request.bookingCode()).stream()
                .filter(p -> p.getDocumentNumber().equalsIgnoreCase(request.documentNumber()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Пассажир с документом " + request.documentNumber() + " не найден в данной брони"));

        Integer flightId = boardingPassRepository.findFlightIdByTicketNo(passenger.getTicketNo());
        if (flightId == null) {
            throw new ResourceNotFoundException("Для данного билета не найден активный рейс");
        }

        if (boardingPassRepository.findByTicketNoAndFlightId(passenger.getTicketNo(), flightId).isPresent()) {
            throw new BusinessException("Пассажир уже прошел регистрацию на этот рейс");
        }

        Integer nextBoardingNo = boardingPassRepository.getNextBoardingNo(flightId);

        String gate = "A-" + (10 + (int)(Math.random() * 15));
        LocalDateTime boardingTime = LocalDateTime.now().plusHours(2);

        BoardingPass boardingPass = new BoardingPass(
                passenger.getTicketNo(),
                flightId,
                nextBoardingNo,
                request.seatNumber(),
                gate,
                null
        );
        boardingPassRepository.save(boardingPass);

        String fullName = passenger.getFirstName() + " " + passenger.getLastName();
        String flightNo = bookingRepository.findFlightNoByBookRef(request.bookingCode());

        return new BoardingPassDTO(
                fullName,
                flightNo != null ? flightNo : "UNKNOWN",
                passenger.getTicketNo(),
                boardingPass.getSeatNumber(),
                gate,
                boardingTime
        );
    }
}