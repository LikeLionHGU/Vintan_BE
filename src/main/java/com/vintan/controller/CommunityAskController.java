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
@RequestMapping("/regions/{regionId}/community") // Base URL path for all endpoints in this controller
public class CommunityAskController {

    private final CommunityAskService communityAskService; // Service layer for community ask operations

    /**
     * GET /regions/{regionId}/community/ask
     * Retrieves a list of community questions for a specific region.
     *
     * @param regionId ID of the region
     * @return ResponseEntity containing AskResponseDto with question list
     */
    @GetMapping("/ask")
    public ResponseEntity<AskResponseDto> getAskList(
            @PathVariable("regionId") Long regionId
    ) {
        return ResponseEntity.ok(communityAskService.getAskList(regionId));
    }

    /**
     * GET /regions/{regionId}/community/ask/{communityId}
     * Retrieves detailed information for a specific question post.
     *
     * @param regionId ID of the region
     * @param communityId ID of the question post
     * @return ResponseEntity containing AskDetailResponseDto with post details
     */
    @GetMapping("/ask/{communityId}")
    public ResponseEntity<AskDetailResponseDto> getAskDetail(
            @PathVariable("regionId") Long regionId,
            @PathVariable("communityId") Long communityId
    ) {
        return ResponseEntity.ok(communityAskService.getAskDetail(regionId, communityId));
    }

    /**
     * POST /regions/{regionId}/community/ask/write
     * Creates a new community question post.
     *
     * @param regionId ID of the region
     * @param session HttpSession of the current user
     * @param request CreateAskRequestDto containing question details
     * @return ResponseEntity with SimpleSuccessResponseDto indicating success
     */
    @PostMapping("/ask/write")
    public ResponseEntity<SimpleSuccessResponseDto> createAsk(
            @PathVariable("regionId") Long regionId,
            HttpSession session,
            @Valid @RequestBody CreateAskRequestDto request
    ) {
        return ResponseEntity.ok(communityAskService.createAsk(regionId, session, request));
    }

    /**
     * POST /regions/{regionId}/community/{communityId}/ask/comment
     * Adds a comment to a specific community question post.
     *
     * @param regionId ID of the region
     * @param communityId ID of the question post
     * @param session HttpSession of the current user
     * @param request CreateCommentRequestDto containing comment details
     * @return ResponseEntity with SimpleSuccessResponseDto indicating success
     */
    @PostMapping("/{communityId}/ask/comment")
    public ResponseEntity<SimpleSuccessResponseDto> createComment(
            @PathVariable("regionId") Long regionId,
            @PathVariable("communityId") Long communityId,
            HttpSession session,
            @Valid @RequestBody CreateCommentRequestDto request
    ) {
        return ResponseEntity.ok(communityAskService.createComment(regionId, communityId, session, request));
    }
}
