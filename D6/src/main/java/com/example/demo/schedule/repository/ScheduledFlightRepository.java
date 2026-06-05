package com.example.demo.schedule.repository;

import com.example.demo.schedule.entity.ScheduledFlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ScheduledFlightRepository extends JpaRepository<ScheduledFlight, String> {

    @Query(value = """
        SELECT 
            route_no, 
            departure_airport, 
            arrival_airport, 
            airplane_code,
            to_char(scheduled_time, 'HH24:MI:SS') as departure_time,
            to_char(scheduled_time + duration, 'HH24:MI:SS') as arrival_time
        FROM bookings.routes 
        WHERE arrival_airport = :destinationIata
        """, nativeQuery = true)
    List<ScheduledFlight> findByDestinationIata(@Param("destinationIata") String destinationIata);

    @Query(value = """
        SELECT 
            route_no, 
            departure_airport, 
            arrival_airport, 
            airplane_code,
            to_char(scheduled_time, 'HH24:MI:SS') as departure_time,
            to_char(scheduled_time + duration, 'HH24:MI:SS') as arrival_time
        FROM bookings.routes 
        WHERE departure_airport = :originIata
        """, nativeQuery = true)
    List<ScheduledFlight> findByOriginIata(@Param("originIata") String originIata);
}