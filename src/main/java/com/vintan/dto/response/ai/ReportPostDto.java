package com.vintan.dto.response.ai;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO used for responding with the ID of a created report.
 */
@Getter
@Setter
public class ReportPostDto {

    private Long reportId; // ID of the report that was created
}
