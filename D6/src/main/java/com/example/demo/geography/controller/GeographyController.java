package com.example.demo.geography.controller;

import com.example.demo.geography.dto.AirportDTO;
import com.example.demo.geography.dto.CityDTO;
import com.example.demo.geography.service.GeographyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "География")
@RestController
@RequestMapping("/geography")
@RequiredArgsConstructor
@Validated
public class GeographyController {
    private final GeographyService geographyService;

    @GetMapping("/cities")
    @Operation(summary = "Получить список всех городов")
    public ResponseEntity<List<CityDTO>> getAllCities(){
        List<CityDTO> cities = geographyService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/airport")
    @Operation(summary = "Получить список всех аэропортов")
    public ResponseEntity<List<AirportDTO>> getAllAirports(){
        List<AirportDTO> airports = geographyService.getAllAirports();
        return ResponseEntity.ok(airports);
    }

    @GetMapping("/cities/{city_id}/airports")
    @Operation(summary = "Получить аэропорты конкретного города")
    public ResponseEntity<List<AirportDTO>> getAirportsByCityId(
            @PathVariable("city_id")
            @NotBlank
            @Parameter(description = "ID или код города", example = "MOW") String cityId){
        List<AirportDTO> airports = geographyService.getAirportsByCityId(cityId);
        return ResponseEntity.ok(airports);
    }
}