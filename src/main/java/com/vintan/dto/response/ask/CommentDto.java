package com.vintan.dto.response.ask;

import com.vintan.domain.QnaComment;
import lombok.*;
import java.time.format.DateTimeFormatter;

/**
 * DTO representing a single comment in a Q&A post.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    /** Unique ID of the comment */
    private Long id;

    /** ID of the user who wrote the comment */
    private String userId;

    /** Content of the comment */
    private String content;

    /** Date of the comment in yyyy.MM.dd format */
    private String date;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    /**
     * Converts a QnaComment entity to a CommentDto.
     *
     * @param c the QnaComment entity
     * @return the CommentDto
     */
    public static CommentDto from(QnaComment c) {
        return CommentDto.builder()
                .id(c.getId())
                .userId(c.getUser().getId())
                .content(c.getText())
                .date(c.getTextTime() == null ? null : c.getTextTime().format(FMT))
                .build();
    }
}
