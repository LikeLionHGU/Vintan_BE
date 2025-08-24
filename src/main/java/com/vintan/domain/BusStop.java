package com.vintan.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity representing a Bus Stop.
 * Stores the station name, served routes, and geographical coordinates.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusStop {

    @Id
    private String stationName; // Primary key: Unique name of the bus station

    private String routeNames;  // Comma-separated names of bus routes that stop here

    private double latitude;    // Latitude coordinate for geolocation

    private double longitude;   // Longitude coordinate for geolocation
}
