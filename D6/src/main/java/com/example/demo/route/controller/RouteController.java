package com.example.demo.route.controller;

import com.example.demo.route.dto.RouteResponseDTO;
import com.example.demo.route.service.RouteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Поиск маршрутов")
@RestController
@RequestMapping("routes")
@RequiredArgsConstructor
@Validated
public class RouteController {

    private final RouteService routeService;

    @GetMapping
    public ResponseEntity<List<RouteResponseDTO>> searchRoutes(
            @RequestParam @NotBlank @Size(min = 3, max = 3) String origin,
            @RequestParam @NotBlank @Size(min = 3, max = 3) String destination,
            @RequestParam("departure_date") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(value = "booking_class", required = false) String bookingClass,
            @RequestParam(value = "max_connections", required = false, defaultValue = "0") @Min(0) Integer maxConnections,
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