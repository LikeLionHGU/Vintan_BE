package com.vintan.dto.request.ai.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Data Transfer Object representing a company.
 * Used for sending company details in AI-related requests.
 */
@Getter
@AllArgsConstructor
@ToString
public class CompanyDto {

    private String name;        // Name of the company
    private String address;     // Address of the company
    private String description; // Description of the company
}
