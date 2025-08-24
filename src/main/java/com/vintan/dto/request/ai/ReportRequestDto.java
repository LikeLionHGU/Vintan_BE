package com.vintan.dto.request.ai;

import lombok.Getter;

/**
 * DTO for requesting an AI-generated report.
 * Contains information about the location and user details.
 */
@Getter
public class ReportRequestDto {

    private String address;        // Full address of the location
    private String categoryCode;   // Category code (e.g., FD6 for restaurants)
    private double pyeong;         // Size of the location in pyeong (Korean unit of area)
    private String userDetail;     // Additional details provided by the user
    private String addressName;    // Name of the address/location
}
