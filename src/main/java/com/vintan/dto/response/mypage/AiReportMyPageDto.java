package com.vintan.dto.response.mypage;

import lombok.*;

/**
 * DTO for displaying AI-generated report info on the MyPage screen.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiReportMyPageDto {

    private Long id;           // Report ID

    private String address;      // Location (address from the latest report)

    private Integer reportCount; // Number of reports

    private String date;         // Report creation date (formatted as yyyy.MM.dd)
}
