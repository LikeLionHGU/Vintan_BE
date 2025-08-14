package com.vintan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 질문 게시판 상세보기 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AskDetailDto {

    private Integer id;              // 게시글 고유 ID
    private String title;            // 글 제목
    private String content;          // 글 내용
    private String userId;           // 작성자 ID
    private String date;             // 작성 날짜 (yyyy.MM.dd)

    private List<CommentDto> commentList = new ArrayList<>(); // 댓글 목록
}