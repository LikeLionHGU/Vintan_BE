package com.vintan.dto;

import com.vintan.domain.QnaComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.FormatProcessor.FMT;

/**
 * 댓글 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private Integer id;       // 댓글 고유 ID
    private String userId;    // 댓글 작성자 ID
    private String content;   // 댓글 내용
    private String date;      // 댓글 작성 날짜 (yyyy.MM.dd)

    /** Entity -> DTO */
    public static CommentDto from(QnaComment c) {
        if (c == null) return null;

        String date = null;
        if (c.getCreatedAt() != null) {
            date = c.getCreatedAt().format(FMT);
        } else if (c.getTextTime() != null) {
            date = c.getTextTime().format(FMT);
        }

        return CommentDto.builder()
                .id(c.getId())
                .userId((c.getUser() != null ? c.getUser().getUserid() : null))
                .content(c.getText())
                .date(date)
                .build();
    }

    /** List<Entity> -> List<DTO> */
    public static List<CommentDto> fromList(List<QnaComment> comments) {
        if (comments == null || comments.isEmpty()) return Collections.emptyList();
        return comments.stream()
                .map(CommentDto::from)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}