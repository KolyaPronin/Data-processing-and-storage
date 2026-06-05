package com.example.demo.booking.repository;

import com.example.demo.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface BookingRepository extends JpaRepository<Booking, String> {

    @Modifying
    @Query(value = "INSERT INTO bookings.bookings (book_ref, book_date, total_amount) VALUES (:bookRef, :bookDate, :totalAmount)", nativeQuery = true)
    void insertBooking(@Param("bookRef") String bookRef, @Param("bookDate") OffsetDateTime bookDate, @Param("totalAmount") BigDecimal totalAmount);

    @Modifying
    @Query(value = "INSERT INTO bookings.segments (ticket_no, flight_id, fare_conditions, price) VALUES (:ticketNo, (SELECT flight_id FROM bookings.flights WHERE route_no = :flightNo LIMIT 1), :fareClass, :amount)", nativeQuery = true)
    void linkTicketToFlight(@Param("ticketNo") String ticketNo, @Param("flightNo") String flightNo, @Param("fareClass") String fareClass, @Param("amount") double amount);

    @Query(value = "SELECT f.route_no FROM bookings.segments s JOIN bookings.flights f ON s.flight_id = f.flight_id JOIN bookings.tickets t ON s.ticket_no = t.ticket_no WHERE t.book_ref = :bookRef LIMIT 1", nativeQuery = true)
    String findFlightNoByBookRef(@Param("bookRef") String bookRef);
}