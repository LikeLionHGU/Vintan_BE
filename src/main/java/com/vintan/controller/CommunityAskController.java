package com.vintan.controller;

import com.vintan.dto.request.ask.CreateAskRequestDto;
import com.vintan.dto.request.ask.CreateCommentRequestDto;
import com.vintan.dto.response.ask.AskDetailResponseDto;
import com.vintan.dto.response.ask.AskResponseDto;
import com.vintan.dto.response.ask.SimpleSuccessResponseDto;
import com.vintan.service.CommunityAskService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/regions/{regionsId}/community")
public class CommunityAskController {

    private final CommunityAskService communityAskService;

    /** 대표화면 목록: GET /regions/{regionsId}/community/ask */
    @GetMapping("/ask")
    public ResponseEntity<AskResponseDto> getAskList(
            @PathVariable("regionsId") Long regionId
    ) {
        return ResponseEntity.ok(communityAskService.getAskList(regionId));
    }

    /** 상세보기: GET /regions/{regionsId}/community/ask/{communityId} */
    @GetMapping("/ask/{communityId}")
    public ResponseEntity<AskDetailResponseDto> getAskDetail(
            @PathVariable("regionsId") Long regionId,
            @PathVariable("communityId") Long postId
    ) {
        return ResponseEntity.ok(communityAskService.getAskDetail(regionId, postId));
    }

    /** 글 작성: POST /regions/{regionsId}/community/ask/write */
    @PostMapping("/ask/write")
    public ResponseEntity<SimpleSuccessResponseDto> createAsk(
            @PathVariable("regionsId") Long regionId,
            HttpSession session,
            @Valid @RequestBody CreateAskRequestDto request
    ) {
        return ResponseEntity.ok(communityAskService.createAsk(regionId, session, request));
    }

    /** 댓글 작성: POST /regions/{regionsId}/community/{communityId}/ask/comment */
    @PostMapping("/{communityId}/ask/comment")
    public ResponseEntity<SimpleSuccessResponseDto> createComment(
            @PathVariable("regionsId") Long regionId,
            @PathVariable("communityId") Long postId,
            HttpSession session,
            @Valid @RequestBody CreateCommentRequestDto request
    ) {
        return ResponseEntity.ok(communityAskService.createComment(regionId, postId, session, request));
    }
}

