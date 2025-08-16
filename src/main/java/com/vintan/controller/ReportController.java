package com.vintan.controller;

import com.vintan.dto.request.ai.ReportRequestDto;
import com.vintan.dto.response.ai.AiReportResponseDto;
import com.vintan.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/reports")
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<AiReportResponseDto> generateReport(@RequestBody ReportRequestDto reportRequestDto) {
        AiReportResponseDto report = reportService.generateReport(reportRequestDto.getAddress(), reportRequestDto.getCategoryCode());
        return ResponseEntity.ok(report);
    }
}
