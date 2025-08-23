package com.vintan.dto.response.ai.reportresponse;

import com.vintan.domain.Competitor;
import lombok.Getter;

/**
 * DTO for returning competitor information in API responses.
 */
@Getter
public class CompetitorResponseDto {

    private String name;        // Competitor name
    private String address;     // Competitor address
    private String description; // Competitor description or details

    /**
     * Constructor that maps a Competitor entity to this DTO.
     * @param competitor the Competitor entity
     */
    public CompetitorResponseDto(Competitor competitor) {
        this.name = competitor.getName();
        this.address = competitor.getAddress();
        this.description = competitor.getDescription();
    }
}
