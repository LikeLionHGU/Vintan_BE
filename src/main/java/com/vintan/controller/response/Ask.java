package com.vintan.controller.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ask {

    private Integer id;               // 게시글 고유 ID
    private String title;             // 글 제목
    private String userId;            // 작성자 ID
    private Integer numberOfComments; // 댓글 수
    private String date;              // 글 작성 날짜 (yyyy.MM.dd)
}