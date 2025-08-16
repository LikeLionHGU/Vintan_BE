package com.vintan.dto.response.ask;

import com.vintan.domain.QnaPost;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class AskDto {

    private Long id;                  // Long으로 변경
    private String title;
    private String userId;
    private Integer numberOfComments;
    private String date;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static AskDto from(QnaPost post) {
        int commentCount = (post.getComments() == null) ? 0 : post.getComments().size();
        String dateStr = (post.getCreatedAt() != null) ? post.getCreatedAt().format(DATE_FMT) : null;
        return AskDto.builder()
                .id(post.getPostId())  // Long 그대로 매핑
                .title(post.getTitle())
                .userId(post.getUser().getId())
                .numberOfComments(commentCount)
                .date(dateStr)
                .build();
    }
}
