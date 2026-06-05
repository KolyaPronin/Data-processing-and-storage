package com.example.demo.checkin.repository;

import com.example.demo.checkin.entity.BoardingPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface BoardingPassRepository extends JpaRepository<BoardingPass, String> {

    Optional<BoardingPass> findByTicketNoAndFlightId(String ticketNo, Integer flightId);

    @Query(value = "SELECT COALESCE(MAX(boarding_no), 0) + 1 FROM bookings.boarding_passes WHERE flight_id = :flightId", nativeQuery = true)
    Integer getNextBoardingNo(@Param("flightId") Integer flightId);

    @Query(value = "SELECT flight_id FROM bookings.segments WHERE ticket_no = :ticketNo LIMIT 1", nativeQuery = true)
    Integer findFlightIdByTicketNo(@Param("ticketNo") String ticketNo);
}