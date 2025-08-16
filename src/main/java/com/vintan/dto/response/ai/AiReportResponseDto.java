package com.vintan.dto.response.ai;

import com.vintan.dto.aiReport.CompanyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AiReportResponseDto {
    private List<CompanyDto> competitors;
    private String overallSummary;
}
