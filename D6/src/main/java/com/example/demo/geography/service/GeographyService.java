package com.example.demo.geography.service;

import com.example.demo.geography.dto.AirportDTO;
import com.example.demo.geography.dto.CityDTO;

import java.util.List;

public interface GeographyService {

    List<CityDTO> getAllCities();

    List<AirportDTO> getAllAirports();

    List<AirportDTO> getAirportsByCityId(String cityId);
}
