package com.vintan.controller;

import com.vintan.domain.BlindCommunityPost;
import com.vintan.dto.request.community.CommunityPostRequestDto;
import com.vintan.dto.response.community.CommunityAllReviewResponseDto;
import com.vintan.dto.response.community.CommunityBlindPostResponseDto;
import com.vintan.dto.response.community.CommunityDetailResponseDto;
import com.vintan.dto.response.user.SessionUserDto;
import com.vintan.service.BlindCommunityPostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing blind community posts (anonymous reviews)
 * in a specific region. Supports CRUD operations: create, edit, delete, and read posts.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/regions/{regionId}/community/blind/reviews") // Base URL path
public class CommunityBlindController {

    private final BlindCommunityPostService blindCommunityPostService;

    /**
     * POST /regions/{regionId}/community/blind/reviews
     * Creates a new blind community post.
     * Requires a logged-in user session.
     *
     * @param regionId the ID of the region
     * @param requestDto the post request data
     * @param request the HttpServletRequest to retrieve session user
     * @return ResponseEntity indicating success (1) or failure (0)
     */
    @PostMapping
    public ResponseEntity<CommunityBlindPostResponseDto> createCommunityPost(
            @PathVariable Long regionId,
            @RequestBody CommunityPostRequestDto requestDto,
            HttpServletRequest request) {

        SessionUserDto sessionUser = getSessionUser(request);

        // Return UNAUTHORIZED if user is not logged in
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommunityBlindPostResponseDto(0));
        }

        try {
            BlindCommunityPost savedPost = blindCommunityPostService.createPost(
                    requestDto, sessionUser.getId(), regionId);
            return ResponseEntity.ok(new CommunityBlindPostResponseDto(1));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommunityBlindPostResponseDto(0));
        }
    }

    /**
     * PATCH /regions/{regionId}/community/blind/reviews/{reviewId}
     * Edits an existing blind community post.
     * Only the post owner can edit.
     *
     * @param regionId the ID of the region
     * @param reviewId the ID of the post to edit
     * @param requestDto the updated post data
     * @param request the HttpServletRequest to retrieve session user
     * @return ResponseEntity indicating success (1) or failure (0)
     */
    @PatchMapping("/{reviewId}")
    public ResponseEntity<CommunityBlindPostResponseDto> editCommunityPost(
            @PathVariable Long regionId,
            @PathVariable Long reviewId,
            @RequestBody CommunityPostRequestDto requestDto,
            HttpServletRequest request) {

        SessionUserDto sessionUser = getSessionUser(request);

        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommunityBlindPostResponseDto(0));
        }

        try {
            blindCommunityPostService.updatePost(reviewId, requestDto, sessionUser.getId());
            return ResponseEntity.ok(new CommunityBlindPostResponseDto(1));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new CommunityBlindPostResponseDto(0));
        }
    }

    /**
     * DELETE /regions/{regionId}/community/blind/reviews/{reviewId}
     * Deletes an existing blind community post.
     * Only the post owner can delete.
     *
     * @param regionId the ID of the region
     * @param reviewId the ID of the post to delete
     * @param request the HttpServletRequest to retrieve session user
     * @return ResponseEntity indicating success (1) or failure (0)
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<CommunityBlindPostResponseDto> deleteCommunityPost(
            @PathVariable Long regionId,
            @PathVariable Long reviewId,
            HttpServletRequest request) {

        SessionUserDto sessionUser = getSessionUser(request);

        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommunityBlindPostResponseDto(0));
        }

        try {
            blindCommunityPostService.deletePost(reviewId, sessionUser.getId());
            return ResponseEntity.ok(new CommunityBlindPostResponseDto(1));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new CommunityBlindPostResponseDto(0));
        }
    }

    /**
     * GET /regions/{regionId}/community/blind/reviews/{reviewId}
     * Retrieves detailed information for a specific blind post.
     *
     * @param regionId the ID of the region
     * @param reviewId the ID of the post
     * @return ResponseEntity with CommunityDetailResponseDto or NOT_FOUND
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<CommunityDetailResponseDto> getCommunityDetail(
            @PathVariable Long regionId,
            @PathVariable Long reviewId) {
        try {
            CommunityDetailResponseDto responseDto = blindCommunityPostService.getPost(reviewId);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * GET /regions/{regionId}/community/blind/reviews
     * Retrieves all blind posts in a region.
     *
     * @param regionId the ID of the region
     * @return ResponseEntity with CommunityAllReviewResponseDto containing all posts
     */
    @GetMapping
    public ResponseEntity<CommunityAllReviewResponseDto> getAllPosts(
            @PathVariable Long regionId) {
        CommunityAllReviewResponseDto responseDto = blindCommunityPostService.getAllPost(regionId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Retrieves the currently logged-in user from the session.
     *
     * @param request HttpServletRequest to access the session
     * @return SessionUserDto of logged-in user, or null if not logged in
     */
    private SessionUserDto getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (SessionUserDto) session.getAttribute("loggedInUser");
        }
        return null;
    }
}
