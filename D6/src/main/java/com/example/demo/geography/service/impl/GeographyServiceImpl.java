package com.example.demo.geography.service.impl;

import com.example.demo.geography.dto.AirportDTO;
import com.example.demo.geography.dto.CityDTO;
import com.example.demo.geography.entity.Airport;
import com.example.demo.geography.repository.AirportRepository;
import com.example.demo.geography.service.GeographyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeographyServiceImpl implements GeographyService {

    private final AirportRepository airportRepository;

    @Override
    public List<CityDTO> getAllCities() {
        return airportRepository.findAllUniqueCities().stream()
                .map(cityName -> new CityDTO(
                        cityName,       // id
                        cityName,       // name
                        "Russia",       // country
                        "RU",           // countryCode
                        "Asia/Novosibirsk" // timezone по умолчанию для DTO
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<AirportDTO> getAllAirports() {
        return airportRepository.findAll().stream()
                .map(this::mapToAirportDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AirportDTO> getAirportsByCityId(String cityId) {
        return airportRepository.findByCityId(cityId).stream()
                .map(this::mapToAirportDto)
                .collect(Collectors.toList());
    }

    private AirportDTO mapToAirportDto(Airport airport) {
        return new AirportDTO(
                airport.getIata(),
                airport.getName(),
                airport.getCityId(),
                airport.getCityName(),
                airport.getCountryCode(),
                airport.getLat(),
                airport.getLon(),
                airport.getTimezone()
        );
    }
}