package com.example.demo.geography.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.Map;

@Entity
@Table(name = "airports_data", schema = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Airport {

    @Id
    @Column(name = "airport_code", length = 3, columnDefinition = "bpchar")
    private String iata;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "airport_name", nullable = false)
    private Map<String, String> airportNameMap;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "city", nullable = false)
    private Map<String, String> cityMap;

    @Column(name = "coordinates", columnDefinition = "point", nullable = false)
    private String coordinates; // Читаем как обычную строку "(lon,lat)"

    @Column(name = "timezone", nullable = false)
    private String timezone;

    public String getName() {
        return airportNameMap != null ? airportNameMap.get("en") : null;
    }

    public String getCityName() {
        return cityMap != null ? cityMap.get("en") : null;
    }

    public String getCityId() {
        return cityMap != null ? cityMap.get("en") : null;
    }

    public String getCountryCode() {
        return "RU";
    }

    public Double getLon() {
        if (coordinates != null) {
            String clean = coordinates.replace("(", "").replace(")", "");
            return Double.parseDouble(clean.split(",")[0]);
        }
        return 0.0;
    }

    public Double getLat() {
        if (coordinates != null) {
            String clean = coordinates.replace("(", "").replace(")", "");
            return Double.parseDouble(clean.split(",")[1]);
        }
        return 0.0;
    }
}