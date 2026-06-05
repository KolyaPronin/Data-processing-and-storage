package com.example.demo.schedule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "routes", schema = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledFlight {

    @Id
    @Column(name = "route_no")
    private String flightNo;

    @Column(name = "departure_airport", length = 3, columnDefinition = "bpchar")
    private String originIata;

    @Column(name = "arrival_airport", length = 3, columnDefinition = "bpchar")
    private String destinationIata;

    @Column(name = "airplane_code", length = 3, columnDefinition = "bpchar")
    private String operatedBy;

    private String departureTime;
    private String arrivalTime;

    public String getOriginName() {
        return "Airport " + originIata;
    }

    public String getDestinationName() {
        return "Airport " + destinationIata;
    }
}