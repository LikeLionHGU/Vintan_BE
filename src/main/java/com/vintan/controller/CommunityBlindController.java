package com.vintan.controller;

import com.vintan.domain.BlindCommunityPost;
import com.vintan.dto.request.community.CommunityPostRequestDto;
import com.vintan.dto.response.community.CommunityBlindPostResponseDto;
import com.vintan.dto.response.user.SessionUserDto;
import com.vintan.service.BlindCommunityPostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/regions/{regionId}/community/blind/reviews")
public class CommunityBlindController {

    private final BlindCommunityPostService blindCommunityPostService;

    @PostMapping
    public ResponseEntity<CommunityBlindPostResponseDto> createCommunityPost(
            @PathVariable Long regionId,
            @RequestBody CommunityPostRequestDto requestDto,
            HttpServletRequest request) {

        SessionUserDto sessionUser = getSessionUser(request);

        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommunityBlindPostResponseDto(0));
        }

        try {
            BlindCommunityPost savedPost = blindCommunityPostService.createPost(requestDto, sessionUser.getId(), regionId);
            return ResponseEntity.ok(new CommunityBlindPostResponseDto(1));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommunityBlindPostResponseDto(0));
        }
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<CommunityBlindPostResponseDto> editCommunityPost(
            @PathVariable Long regionId,
            @PathVariable Long reviewId,
            @RequestBody CommunityPostRequestDto requestDto,
            HttpServletRequest request) {
        SessionUserDto sessionUser = getSessionUser(request);
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommunityBlindPostResponseDto(0));
        }

        try {
            blindCommunityPostService.updatePost(reviewId, requestDto, sessionUser.getId());
            return ResponseEntity.ok(new CommunityBlindPostResponseDto(1));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CommunityBlindPostResponseDto(0));
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<CommunityBlindPostResponseDto> deleteCommunityPost(
            @PathVariable Long regionId,
            @PathVariable Long reviewId,
            HttpServletRequest request) {
        SessionUserDto sessionUser = getSessionUser(request);

        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommunityBlindPostResponseDto(0));
        }

        try {
            blindCommunityPostService.deletePost(reviewId, sessionUser.getId());
            return ResponseEntity.ok(new CommunityBlindPostResponseDto(1));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CommunityBlindPostResponseDto(0));
        }
    }

    private SessionUserDto getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (SessionUserDto) session.getAttribute("loggedInUser");
        }
        return null;
    }
}

