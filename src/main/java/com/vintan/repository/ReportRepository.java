package com.vintan.repository;

import com.vintan.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Report entities.
 * Provides basic CRUD operations along with some custom queries.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    /**
     * Count the number of reports for a given user.
     *
     * @param userId ID of the user
     * @return number of reports
     */
    long countByUser_Id(String userId);

    /**
     * Get the most recent report for a given user, ordered by registration date descending.
     *
     * @param userId ID of the user
     * @return most recent Report or null if none
     */
    List<Report> findByUser_IdOrderByRegDateDesc(String userId);

    /**
     * Retrieve a report by its ID.
     *
     * @param reportId ID of the report
     * @return Report entity
     */
    Report getReportById(Long reportId);
}
