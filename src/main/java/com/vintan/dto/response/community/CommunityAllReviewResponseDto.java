package com.vintan.dto.response.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/**
 * DTO representing the summary of all blind community reviews in a region.
 */
@Getter
@AllArgsConstructor
public class CommunityAllReviewResponseDto {

    private double totalRate;        // Average overall rating
    private double clean;            // Average cleanliness rating
    private double people;           // Average foot traffic / people rating
    private double accessibility;    // Average accessibility rating
    private double rentFee;          // Average rent/lease rating
    private List<BlindSummaryDto> blind; // List of individual blind community posts
    private String addressName;
}
