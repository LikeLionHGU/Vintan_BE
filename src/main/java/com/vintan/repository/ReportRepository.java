package com.vintan.repository;

import com.vintan.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    long countByUser_Id(String userId);
    Report findTop1ByUser_IdOrderByRegDateDesc(String userId);

    Report getReportById(Long reportId);
}
