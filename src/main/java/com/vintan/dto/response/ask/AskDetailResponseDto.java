package com.vintan.dto.response.ask;

import com.vintan.domain.QnaComment;
import com.vintan.domain.QnaPost;
import lombok.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AskDetailResponseDto {

    private Long id;                  // Post ID
    private String title;             // Post title
    private String content;           // Post content
    private String userId;            // ID of the user who wrote the post
    private String date;              // Post creation date in yyyy.MM.dd format
    private List<CommentDto> commentList;  // List of comments

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    /**
     * Converts a QnaPost entity and its comments into an AskDetailResponseDto.
     *
     * @param p        The QnaPost entity
     * @param comments The list of QnaComment entities
     * @return AskDetailResponseDto
     */
    public static AskDetailResponseDto from(QnaPost p, List<QnaComment> comments) {
        return AskDetailResponseDto.builder()
                .id(p.getPostId())  // Map post ID
                .title(p.getTitle())  // Map post title
                .content(p.getContent())  // Map post content
                .userId(p.getUser().getId())  // Map user ID
                .date(p.getCreatedAt() == null ? null : p.getCreatedAt().format(FMT))  // Format creation date
                .commentList(comments.stream().map(CommentDto::from).toList())  // Map comments
                .build();
    }
}
