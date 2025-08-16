package com.vintan.service;

import com.vintan.domain.BlindCommunityPost;
import com.vintan.domain.User;
import com.vintan.dto.request.community.CommunityPostRequestDto;
import com.vintan.dto.response.community.BlindSummaryDto;
import com.vintan.dto.response.community.CommunityAllReviewResponseDto;
import com.vintan.dto.response.community.CommunityBlindDetailResponseDto;
import com.vintan.dto.response.community.CommunityDetailResponseDto;
import com.vintan.embedded.CategoryRate;
import com.vintan.repository.BlindCommunityPostRepository;
import com.vintan.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlindCommunityPostService {

    private final BlindCommunityPostRepository blindCommunityPostRepository;
    private final UserRepository userRepository;

    @Transactional
    public BlindCommunityPost createPost(CommunityPostRequestDto requestDto, String userId, Long regionId) {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("no writer information"));

        BlindCommunityPost newPost = BlindCommunityPost.builder()
                .title(requestDto.getTitle())
                .positive(requestDto.getPositive())
                .negative(requestDto.getNegative())
                .address(requestDto.getAddress())
                .categoryRate(requestDto.getCategoryRate())
                .regionNo(regionId)
                .user(writer)
                .build();

        return blindCommunityPostRepository.save(newPost);
    }

    @Transactional
    public void updatePost(Long reviewId, CommunityPostRequestDto requestDto, String userId) {
        BlindCommunityPost post = blindCommunityPostRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("No such post"));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("No right for this user");
        }

        post.setTitle(requestDto.getTitle());
        post.setPositive(requestDto.getPositive());
        post.setNegative(requestDto.getNegative());
        post.setAddress(requestDto.getAddress());
        post.setCategoryRate(requestDto.getCategoryRate());

        blindCommunityPostRepository.save(post);
    }

    @Transactional
    public void deletePost(Long reviewId, String userId) {
        BlindCommunityPost post = blindCommunityPostRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("No such post"));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("No right for this user");
        }

        blindCommunityPostRepository.delete(post);
    }

    public CommunityDetailResponseDto getPost(Long reviewId) {
        BlindCommunityPost post = blindCommunityPostRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("No such post"));
        CategoryRate rate = post.getCategoryRate();

        double totalRate = (double) (rate.getCleanness() + rate.getPeople() + rate.getReach() + rate.getRentFee()) / 4;

        CommunityBlindDetailResponseDto blindDetailDto = new CommunityBlindDetailResponseDto(post);

        return new  CommunityDetailResponseDto(totalRate, blindDetailDto);
    }

    public CommunityAllReviewResponseDto getAllPost(Long regionId) {
        List<BlindCommunityPost> posts = blindCommunityPostRepository.findByRegionNo(regionId);

        if (posts.isEmpty()) {
            return new CommunityAllReviewResponseDto(0.0, 0.0, 0.0, 0.0, 0.0, List.of());
        }

        List<BlindSummaryDto> blindSummaries = posts.stream()
                .map(BlindSummaryDto::new)
                .collect(Collectors.toList());

        double totalRateAvg = posts.stream()
                .mapToDouble(post -> (post.getCategoryRate().getCleanness() + post.getCategoryRate().getPeople() +
                        post.getCategoryRate().getReach() + post.getCategoryRate().getRentFee()) / 4.0)
                .average()
                .orElse(0.0);
        double cleanAvg = posts.stream().mapToDouble(p -> p.getCategoryRate().getCleanness()).average().orElse(0.0);
        double peopleAvg = posts.stream().mapToDouble(p -> p.getCategoryRate().getPeople()).average().orElse(0.0);
        double accessibilityAvg = posts.stream().mapToDouble(p -> p.getCategoryRate().getReach()).average().orElse(0.0);
        double rentFeeAvg = posts.stream().mapToDouble(p -> p.getCategoryRate().getRentFee()).average().orElse(0.0);

        return new CommunityAllReviewResponseDto(totalRateAvg, cleanAvg, peopleAvg, accessibilityAvg, rentFeeAvg, blindSummaries);
    }

}
