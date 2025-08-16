package com.vintan.dto.response.ai;

import com.vintan.domain.Competitor;
import com.vintan.dto.aiReport.CompanyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class CompetitorResponseDto {
    private String name;
    private String address;
    private String description;

    public CompetitorResponseDto(Competitor competitor) {
        this.name = competitor.getName();
        this.address = competitor.getAddress();
        this.description = competitor.getDescription();
    }
}
