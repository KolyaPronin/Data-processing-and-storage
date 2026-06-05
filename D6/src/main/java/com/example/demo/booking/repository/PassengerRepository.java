package com.example.demo.booking.repository;

import com.example.demo.booking.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PassengerRepository extends JpaRepository<Passenger, String> {
    @Modifying
    @Query(value = "INSERT INTO bookings.tickets (ticket_no, book_ref, passenger_id, passenger_name, outbound) VALUES (:ticketNo, :bookRef, :passengerId, :passengerName, true)", nativeQuery = true)
    void saveTicket(@Param("ticketNo") String ticketNo, @Param("bookRef") String bookRef, @Param("passengerId") String passengerId, @Param("passengerName") String passengerName);

    List<Passenger> findByBookingCode(String bookingCode);
}