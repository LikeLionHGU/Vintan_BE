package com.vintan.controller;

import com.vintan.dto.request.ask.CreateAskRequestDto;
import com.vintan.dto.request.comment.CreateCommentRequestDto;
import com.vintan.dto.response.ask.AskDetailResponseDto;
import com.vintan.dto.response.ask.AskResponseDto;
import com.vintan.dto.response.ask.SimpleSuccessResponseDto;
import com.vintan.service.CommunityAskService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling community Q&A (Ask) operations.
 * Supports listing, viewing, creating questions, and adding comments.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/regions") // Base URL path for all endpoints in this controller
public class CommunityAskController {

    private final CommunityAskService communityAskService; // Service layer for community ask operations

    /**
     * GET /regions
     * Retrieves a list of community questions (representative screen view).
     *
     * @return ResponseEntity containing AskResponseDto with question list
     */
    @GetMapping
    public ResponseEntity<AskResponseDto> getAskList() {
        return ResponseEntity.ok(communityAskService.getAskList());
    }

    /**
     * GET /regions/{postId}
     * Retrieves detailed information for a specific question post.
     *
     * @param postId ID of the question post
     * @return ResponseEntity containing AskDetailResponseDto with post details
     */
    @GetMapping("/{postId}")
    public ResponseEntity<AskDetailResponseDto> getAskDetail(@PathVariable Long postId) {
        return ResponseEntity.ok(communityAskService.getAskDetail(postId));
    }

    /**
     * POST /regions
     * Creates a new community question post.
     *
     * @param session HttpSession of the current user
     * @param request CreateAskRequestDto containing question details
     * @return ResponseEntity with SimpleSuccessResponseDto indicating success
     */
    @PostMapping
    public ResponseEntity<SimpleSuccessResponseDto> createAsk(
            HttpSession session,
            @Valid @RequestBody CreateAskRequestDto request
    ) {
        return ResponseEntity.ok(communityAskService.createAsk(session, request));
    }

    /**
     * POST /regions/{postId}/comments
     * Adds a comment to a specific community question post.
     *
     * @param postId ID of the question post
     * @param session HttpSession of the current user
     * @param request CreateCommentRequestDto containing comment details
     * @return ResponseEntity with SimpleSuccessResponseDto indicating success
     */
    @PostMapping("/{postId}/comments")
    public ResponseEntity<SimpleSuccessResponseDto> createComment(
            @PathVariable Long postId,
            HttpSession session,
            @Valid @RequestBody CreateCommentRequestDto request
    ) {
        return ResponseEntity.ok(communityAskService.createComment(postId, session, request));
    }
}
