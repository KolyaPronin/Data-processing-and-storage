package com.example.demo.geography.repository;

import com.example.demo.geography.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, String> {

    @Query(value = "SELECT * FROM bookings.airports_data WHERE city->>'en' = :cityId", nativeQuery = true)
    List<Airport> findByCityId(@Param("cityId") String cityId);

    @Query(value = "SELECT DISTINCT city->>'en' FROM bookings.airports_data ORDER BY city->>'en'", nativeQuery = true)
    List<String> findAllUniqueCities();
}
