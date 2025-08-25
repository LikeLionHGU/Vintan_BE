package com.vintan.controller;

import com.vintan.domain.Report;
import com.vintan.dto.request.ai.ReportRequestDto;
import com.vintan.dto.response.ai.ReportPostDto;
import com.vintan.dto.response.ai.ReportResponseDto;
import com.vintan.dto.response.user.SessionUserDto;
import com.vintan.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for AI-generated reports.
 * Provides endpoints to generate reports and fetch them by ID.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/reports") // Base URL for report operations
public class ReportController {

    private final ReportService reportService;

    /**
     * POST /ai/reports/generate/{regionId}
     * Generates a full AI report for a specific region.
     *
     * @param requestDto request data for report generation
     * @param regionId the region for which the report is generated
     * @param request HttpServletRequest to access session
     * @return ResponseEntity containing ReportPostDto with the generated report ID
     */
    @PostMapping("/generate/{regionId}")
    public ResponseEntity<ReportPostDto> generateReport(
            @RequestBody ReportRequestDto requestDto,
            @PathVariable Long regionId,
            HttpServletRequest request) {

        // Get the current session; return 401 Unauthorized if not logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        System.out.println("지금 받은 regiond Id: "+regionId);

        // Retrieve user information from session
        SessionUserDto sessionUser = (SessionUserDto) session.getAttribute("loggedInUser");
        String userId = sessionUser.getId();

        ReportPostDto reportPostDto = new ReportPostDto();

        try {
            // Generate full report using the service
            Report reportEntity = reportService.generateFullReport(requestDto, userId, regionId);

            // Set the generated report ID in the response DTO
            reportPostDto.setReportId(reportEntity.getId());

            // Return 200 OK with the report ID
            return ResponseEntity.ok(reportPostDto);

        } catch (Exception e) {
            e.printStackTrace();
            // In case of error, return reportId = 0 and 500 Internal Server Error
            reportPostDto.setReportId(0L);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(reportPostDto);
        }
    }

    /**
     * GET /ai/reports/{reportId}
     * Fetches a report by its ID.
     *
     * @param reportId the ID of the report
     * @return ResponseEntity containing ReportResponseDto with report details
     */
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponseDto> getReport(@PathVariable Long reportId) {
        // Fetch the report using the service and wrap in DTO
        ReportResponseDto responseDto = new ReportResponseDto(reportService.getReport(reportId));
        return ResponseEntity.ok(responseDto);
    }
}
