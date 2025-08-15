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
@RequestMapping("/regions")
public class CommunityAskController {

    private final CommunityAskService communityAskService;

    /** GET /community/ask : 대표화면 목록 */
    @GetMapping
    public ResponseEntity<AskResponseDto> getAskList() {
        return ResponseEntity.ok(communityAskService.getAskList());
    }

    /** GET /community/ask/{postId} : 상세보기 */
    @GetMapping("/{postId}")
    public ResponseEntity<AskDetailResponseDto> getAskDetail(@PathVariable Long postId) {
        return ResponseEntity.ok(communityAskService.getAskDetail(postId));
    }

    /** POST /community/ask : 질문 글 작성 */
    @PostMapping
    public ResponseEntity<SimpleSuccessResponseDto> createAsk(
            HttpSession session,
            @Valid @RequestBody CreateAskRequestDto request
    ) {
        return ResponseEntity.ok(communityAskService.createAsk(session, request));
    }

    /** POST /community/ask/{postId}/comments : 댓글 작성 */
    @PostMapping("/{postId}/comments")
    public ResponseEntity<SimpleSuccessResponseDto> createComment(
            @PathVariable Long postId,
            HttpSession session,
            @Valid @RequestBody CreateCommentRequestDto request
    ) {
        return ResponseEntity.ok(communityAskService.createComment(postId, session, request));
    }
}

