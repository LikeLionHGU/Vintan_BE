package com.vintan.dto.response.mypage;

import lombok.*;
import java.util.List;

/**
 * DTO for aggregating all MyPage information for a user.
 * Contains user info, recent posts, and AI-generated report summary.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageResponse {

    private String email;                     // User's email
    private String id;                        // User ID
    private String name;                      // User name
    private Integer point;                    // User's points
    private Integer businessNumber;           // Business registration number (if any)

    private BlindMyPageDto blind;             // Latest blind community post by the user (nullable)
    private List<AskMyPageDto> ask;           // Up to 3 recent QnA posts by the user

    private AiReportMyPageDto aiReport;       // AI-generated report summary (nullable)
}
