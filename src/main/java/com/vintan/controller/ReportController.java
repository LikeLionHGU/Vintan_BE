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

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/reports")
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/generate/{regionId}")
    public ResponseEntity<ReportPostDto> generateReport(
            @RequestBody ReportRequestDto requestDto,
            @PathVariable Long regionId,
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        SessionUserDto sessionUser = (SessionUserDto) session.getAttribute("loggedInUser");
        String userId = sessionUser.getId();

        ReportPostDto reportPostDto = new ReportPostDto();

        try {


        Report reportEntity = reportService.generateFullReport(
                requestDto,
                userId,
                regionId
        );

        reportPostDto.setReportId(reportEntity.getId());

        return ResponseEntity.ok(reportPostDto);
        } catch (Exception e) {
            e.printStackTrace();
            reportPostDto.setReportId(0L);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(reportPostDto);
        }
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponseDto> getReport(@PathVariable Long reportId) {
        ReportResponseDto responseDto = new ReportResponseDto(reportService.getReport(reportId));
        return ResponseEntity.ok(responseDto);
    }
}
