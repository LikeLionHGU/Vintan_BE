package com.vintan.repository;

import com.vintan.domain.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing BusStop entities.
 * The primary key is of type String (stationName).
 */
@Repository
public interface BusStopRepository extends JpaRepository<BusStop, String> {
    // No custom queries needed at the moment; basic CRUD is provided by JpaRepository
}
