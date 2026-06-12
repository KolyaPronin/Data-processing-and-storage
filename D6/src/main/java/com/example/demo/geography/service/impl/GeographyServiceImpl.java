package com.example.demo.geography.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.geography.dto.AirportDTO;
import com.example.demo.geography.dto.CityDTO;
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
        List<Object[]> rows = airportRepository.findAllUniqueCitiesNative();
        if (rows.isEmpty()) {
            throw new ResourceNotFoundException("Список городов пуст");
        }
        return rows.stream().map(row -> new CityDTO(
                row[0].toString(),
                row[1].toString(),
                row[2].toString(),
                row[3].toString(),
                row[4].toString()
        )).collect(Collectors.toList());
    }

    @Override
    public List<AirportDTO> getAllAirports() {
        List<AirportDTO> airports = airportRepository.findAll().stream()
                .map(airport -> new AirportDTO(
                        airport.getIata(),
                        airport.getName(),
                        airport.getCityId(),
                        airport.getCityName(),
                        airport.getCountryCode(),
                        airport.getLat(),
                        airport.getLon(),
                        airport.getTimezone()
                )).collect(Collectors.toList());
        if (airports.isEmpty()) {
            throw new ResourceNotFoundException("Список аэропортов пуст");
        }
        return airports;
    }

    @Override
    public List<AirportDTO> getAirportsByCityId(String cityId) {
        if (cityId == null || cityId.isBlank()) {
            throw new ResourceNotFoundException("Идентификатор города не может быть пустым");
        }
        List<Object[]> rows = airportRepository.findByCityIdNative(cityId);
        if (rows.isEmpty()) {
            throw new ResourceNotFoundException("Аэропорты для города с ID '" + cityId + "' не найдены");
        }
        return rows.stream().map(row -> new AirportDTO(
                row[0].toString(),
                row[1].toString(),
                row[2].toString(),
                row[3].toString(),
                row[4].toString(),
                ((Number) row[6]).doubleValue(),
                ((Number) row[5]).doubleValue(),
                row[7].toString()
        )).collect(Collectors.toList());
    }
}