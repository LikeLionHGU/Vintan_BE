package com.vintan.controller;

import com.vintan.domain.BlindCommunityPost;
import com.vintan.domain.User;
import com.vintan.dto.request.community.CommunityPostRequestDto;
import com.vintan.dto.response.community.CommunityBlindPostResponseDto;
import com.vintan.dto.response.user.SessionUserDto;
import com.vintan.repository.BlindCommunityPostRepository;
import com.vintan.repository.UserRepository;
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

    private final BlindCommunityPostRepository blindCommunityPostRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<CommunityBlindPostResponseDto> createCommunityPost(
            @PathVariable Long regionId,
            @RequestBody CommunityPostRequestDto requestDto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loggedInUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommunityBlindPostResponseDto(0));
        }

        try {
            SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute("loggedInUser");
            String userId = sessionUserDto.getId();

            User writer = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No info for writer"));

            BlindCommunityPost newPost = BlindCommunityPost.builder()
                    .title(requestDto.getTitle())
                    .positive(requestDto.getPositive())
                    .negative(requestDto.getNegative())
                    .address(requestDto.getAddress())
                    .categoryRate(requestDto.getCategoryRate())
                    .regionNo(regionId)
                    .user(writer)
                    .build();
            blindCommunityPostRepository.save(newPost);
            return ResponseEntity.ok(new CommunityBlindPostResponseDto(1));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CommunityBlindPostResponseDto(0));
        }
    }
}
