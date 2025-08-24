package com.vintan.dto.response.ask;

import com.vintan.domain.QnaPost;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AskDto {

    private Long id;                  // Post ID
    private String title;             // Post title
    private String userId;            // ID of the user who wrote the post
    private Integer numberOfComments; // Number of comments for this post
    private String date;              // Creation date in yyyy.MM.dd format

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy.MM.dd");

    /**
     * Converts a QnaPost entity to an AskDto.
     *
     * @param post The QnaPost entity
     * @return AskDto
     */
    public static AskDto from(QnaPost post) {
        int commentCount = (post.getComments() == null) ? 0 : post.getComments().size();
        String dateStr = (post.getCreatedAt() != null) ? post.getCreatedAt().format(DATE_FMT) : null;

        return AskDto.builder()
                .id(post.getPostId())           // Map post ID
                .title(post.getTitle())         // Map post title
                .userId(post.getUser().getId()) // Map user ID
                .numberOfComments(commentCount) // Map comment count
                .date(dateStr)                  // Map formatted creation date
                .build();
    }
}
