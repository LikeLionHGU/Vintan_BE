package com.vintan.dto.response.mypage;

import lombok.*;

/**
 * DTO for displaying user's Q&A posts on the MyPage screen.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AskMyPageDto {

    private String id;            // User ID

    private Long askId;

    private String title;         // Question title

    private Integer countComment; // Number of comments

    private String date;          // Post creation date (formatted as yyyy.MM.dd)
}
