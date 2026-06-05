package com.example.demo.geography.controller;

import com.example.demo.geography.dto.AirportDTO;
import com.example.demo.geography.dto.CityDTO;
import com.example.demo.geography.service.GeographyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/geography")
@RequiredArgsConstructor
public class GeographyController {
    private final GeographyService geographyService;

    @GetMapping("/cities")
    public ResponseEntity<List<CityDTO>> getAllCities(){
        List<CityDTO> cities = geographyService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/airport")
    public ResponseEntity<List<AirportDTO>> getAllAirports(){
        List<AirportDTO> airports = geographyService.getAllAirports();
        return ResponseEntity.ok(airports);
    }

    @GetMapping("/cities/{city_id}/airports")
    public ResponseEntity<List<AirportDTO>> getAirportsByCityId(@PathVariable("city_id") String cityId){
        List<AirportDTO> airports = geographyService.getAirportsByCityId(cityId);
        return ResponseEntity.ok(airports);
    }

}
