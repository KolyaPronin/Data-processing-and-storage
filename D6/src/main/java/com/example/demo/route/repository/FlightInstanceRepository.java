package com.example.demo.route.repository;

import com.example.demo.route.entity.FlightInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FlightInstanceRepository extends JpaRepository<FlightInstance, Integer> {

    @Query(value = """
            SELECT DISTINCT
                f.flight_id, 
                f.route_no as flight_no, 
                r.departure_airport, 
                r.arrival_airport, 
                f.scheduled_departure,
                5000.00 as price,
                (
                    (SELECT COUNT(*) FROM bookings.seats s 
                     WHERE s.airplane_code = r.airplane_code 
                       AND s.fare_conditions = :bookingClass) - 
                    (SELECT COUNT(*) FROM bookings.boarding_passes bp 
                     WHERE bp.flight_id = f.flight_id)
                )::int as available_seats
            FROM bookings.flights f
            JOIN bookings.routes r ON f.route_no = r.route_no
            WHERE r.departure_airport = :origin 
              AND r.arrival_airport = :destination 
              AND to_char(f.scheduled_departure, 'YYYY-MM-DD') = :departureDate
            """, nativeQuery = true)
    List<Object[]> searchFlightsNative(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("departureDate") String departureDate,
            @Param("bookingClass") String bookingClass
    );
}