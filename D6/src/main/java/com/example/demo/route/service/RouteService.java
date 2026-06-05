package com.example.demo.route.service;

import com.example.demo.route.dto.RouteResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface RouteService {

    List<RouteResponseDTO> searchRoutes(String origin, String destination, LocalDate departureDate,
                                        String bookingClass, Integer maxConnections, String lang);
}
