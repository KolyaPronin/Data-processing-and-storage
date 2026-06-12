package com.example.demo.route.service.impl;

import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.route.dto.RouteResponseDTO;
import com.example.demo.route.repository.FlightInstanceRepository;
import com.example.demo.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final FlightInstanceRepository flightInstanceRepository;

    @Override
    public List<RouteResponseDTO> searchRoutes(String origin, String destination, LocalDate departureDate,
                                               String bookingClass, Integer maxConnections, String lang) {

        if (origin.equalsIgnoreCase(destination)) {
            throw new BusinessException("Аэропорт отправления и назначения не могут совпадать");
        }

        String actualClass = (bookingClass == null) ? "Economy" : bookingClass;

        List<Object[]> rows = flightInstanceRepository
                .searchFlightsNative(origin, destination, departureDate.toString(), actualClass);

        if (rows.isEmpty()) {
            throw new ResourceNotFoundException("Рейсы по направлению " + origin + " -> " + destination + " на дату " + departureDate + " не найдены");
        }

        return rows.stream().map(row -> {
            LocalDate depDate;
            Object dateObj = row[4];

            if (dateObj instanceof java.time.Instant) {
                depDate = ((java.time.Instant) dateObj).atZone(ZoneId.systemDefault()).toLocalDate();
            } else if (dateObj instanceof java.sql.Timestamp) {
                depDate = ((java.sql.Timestamp) dateObj).toLocalDateTime().toLocalDate();
            } else if (dateObj instanceof java.time.OffsetDateTime) {
                depDate = ((java.time.OffsetDateTime) dateObj).toLocalDate();
            } else if (dateObj instanceof java.time.ZonedDateTime) {
                depDate = ((java.time.ZonedDateTime) dateObj).toLocalDate();
            } else {
                depDate = LocalDate.parse(dateObj.toString().substring(0, 10));
            }

            return new RouteResponseDTO(
                    row[0].toString(),
                    row[1] != null ? row[1].toString() : "UNKNOWN",
                    row[2] != null ? row[2].toString() : origin,
                    row[3] != null ? row[3].toString() : destination,
                    depDate,
                    row[6] != null ? ((Number) row[6]).intValue() : 0,
                    row[5] != null ? ((Number) row[5]).doubleValue() : 5000.0
            );
        }).collect(Collectors.toList());
    }
}