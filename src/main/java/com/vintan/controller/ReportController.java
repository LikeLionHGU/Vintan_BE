package com.vintan.controller;

import com.vintan.domain.Report;
import com.vintan.dto.request.ai.ReportRequestDto;
import com.vintan.dto.response.ai.ReportResponseDto;
import com.vintan.dto.response.user.SessionUserDto;
import com.vintan.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/generate")
    public ResponseEntity<ReportResponseDto> generateReport(@RequestBody ReportRequestDto requestDto, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        SessionUserDto sessionUser = (SessionUserDto) session.getAttribute("loggedInUser");
        String userId = sessionUser.getId();

        Report reportEntity = reportService.generateFullReport(
                requestDto.getAddress(),
                requestDto.getCategoryCode(),
                userId
        );

        ReportResponseDto responseDto = new ReportResponseDto(reportEntity);

        return ResponseEntity.ok(responseDto);
    }
}
