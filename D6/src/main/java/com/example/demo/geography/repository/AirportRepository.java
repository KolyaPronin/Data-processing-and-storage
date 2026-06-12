package com.example.demo.geography.repository;

import com.example.demo.geography.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, String> {

    @Query(value = """
        SELECT 
            airport_code as iata,
            airport_name->>'en' as name,
            city->>'en' as cityId,
            city->>'ru' as cityName,
            CASE 
                WHEN timezone LIKE 'Europe/Moscow' OR timezone LIKE 'Europe/Volgograd' OR timezone LIKE 'Europe/Kaliningrad' OR timezone LIKE 'Asia/Novosibirsk' OR timezone LIKE 'Asia/Krasnoyarsk' OR timezone LIKE 'Asia/Yekaterinburg' THEN 'RU'
                WHEN timezone LIKE 'Europe/%' THEN 'EU'
                WHEN timezone LIKE 'America/%' THEN 'US'
                ELSE 'INT'
            END as countryCode,
            coordinates[0] as lon,
            coordinates[1] as lat,
            timezone
        FROM bookings.airports_data 
        WHERE city->>'en' = :cityId
        """, nativeQuery = true)
    List<Object[]> findByCityIdNative(@Param("cityId") String cityId);

    @Query(value = """
        SELECT DISTINCT ON (city->>'en')
            city->>'en' as id,
            city->>'ru' as name,
            CASE 
                WHEN timezone LIKE 'Europe/Moscow' OR timezone LIKE 'Europe/Volgograd' OR timezone LIKE 'Europe/Kaliningrad' OR timezone LIKE 'Asia/Novosibirsk' OR timezone LIKE 'Asia/Krasnoyarsk' OR timezone LIKE 'Asia/Yekaterinburg' THEN 'Russia'
                WHEN timezone LIKE 'Europe/%' THEN 'Europe'
                WHEN timezone LIKE 'America/%' THEN 'North America'
                ELSE 'International'
            END as country,
            CASE 
                WHEN timezone LIKE 'Europe/Moscow' OR timezone LIKE 'Europe/Volgograd' OR timezone LIKE 'Europe/Kaliningrad' OR timezone LIKE 'Asia/Novosibirsk' OR timezone LIKE 'Asia/Krasnoyarsk' OR timezone LIKE 'Asia/Yekaterinburg' THEN 'RU'
                WHEN timezone LIKE 'Europe/%' THEN 'EU'
                WHEN timezone LIKE 'America/%' THEN 'US'
                ELSE 'INT'
            END as countryCode,
            timezone
        FROM bookings.airports_data 
        ORDER BY city->>'en'
        """, nativeQuery = true)
    List<Object[]> findAllUniqueCitiesNative();
}