package com.vintan.controller.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AskListResponse {

    private List<Ask> askList = new ArrayList<>(); // 질문 게시판 전체 글 목록
}
