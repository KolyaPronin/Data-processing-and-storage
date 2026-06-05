package com.example.demo.route.controller;

import com.example.demo.route.dto.RouteResponseDTO;
import com.example.demo.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/search")
    public ResponseEntity<List<RouteResponseDTO>> searchRoutes(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) String bookingClass,
            @RequestParam(required = false, defaultValue = "0") Integer maxConnections,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang
    ) {
        List<RouteResponseDTO> routes = routeService.searchRoutes(
                origin,
                destination,
                departureDate,
                bookingClass,
                maxConnections,
                lang
        );

        return ResponseEntity.ok(routes);
    }
}
