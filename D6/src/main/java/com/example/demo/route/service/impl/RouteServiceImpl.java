package com.example.demo.route.service.impl;

import com.example.demo.route.dto.RouteResponseDTO;
import com.example.demo.route.repository.FlightInstanceRepository;
import com.example.demo.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final FlightInstanceRepository flightInstanceRepository;

    @Override
    public List<RouteResponseDTO> searchRoutes(String origin, String destination, LocalDate departureDate,
                                               String bookingClass, Integer maxConnections, String lang) {

        List<Object[]> rows = flightInstanceRepository
                .searchFlightsNative(origin, destination, departureDate, bookingClass);

        return rows.stream().map(row -> new RouteResponseDTO(
                row[0].toString(),                                 // id (flight_id)
                (String) row[1],                                   // flightNo
                (String) row[2],                                   // origin
                (String) row[3],                                   // destination
                ((java.time.Instant) row[4]).atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
                ((Number) row[6]).intValue(),                      // availableSeats
                ((Number) row[5]).doubleValue()                    // price
        )).collect(Collectors.toList());
    }
}