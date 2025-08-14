package com.vintan.dto;

import com.vintan.domain.QnaPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 질문 게시판 전체 글 목록 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AskListDto {


    private List<AskDto> askList = new ArrayList<>(); //질문계시판 전체 글 목록

    /**
     * 게시글 목록 + (postId -> 댓글수) 맵 -> AskListDto
     * commentCountByPostId 없으면 0으로 처리
     */
    public static AskListDto from(List<QnaPost> posts, Map<Integer, Integer> commentCountByPostId) {
        if (posts == null || posts.isEmpty()) {
            return AskListDto.builder().askList(new ArrayList<>()).build();
        }
        Map<Integer, Integer> safeMap = (commentCountByPostId != null)
                ? commentCountByPostId : Collections.emptyMap();

        List<AskDto> list = posts.stream()
                .map(p -> AskDto.from(p, safeMap.getOrDefault(p.getPostId(), 0)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return AskListDto.builder().askList(list).build();
    }

}
