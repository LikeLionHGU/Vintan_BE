package com.vintan.dto;

import com.vintan.domain.QnaPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

/**
 * 질문 게시판 목록에서의 단일 글 정보 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AskDto {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private Integer id;             // 게시글 고유 ID
    private String title;           // 글 제목
    private String userId;          // 작성자 ID
    private Integer numberOfComments; // 댓글 수
    private String date;            // 작성 날짜 (yyyy.MM.dd)

    /** Entity + 댓글 수 -> DTO */
    public static AskDto from(QnaPost post, int numberOfComments) {
        if (post == null) return null;
        String date = (post.getCreatedAt() != null) ? post.getCreatedAt().format(FMT) : null;

        return AskDto.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .userId(post.getUserId())
                .numberOfComments(numberOfComments)
                .date(date)
                .build();
    }
}