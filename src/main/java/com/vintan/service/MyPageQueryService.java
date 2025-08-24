package com.vintan.service;

import com.vintan.domain.*;
import com.vintan.dto.response.community.CategoryRateDto;
import com.vintan.dto.response.mypage.AiReportMyPageDto;
import com.vintan.dto.response.mypage.AskMyPageDto;
import com.vintan.dto.response.mypage.BlindMyPageDto;
import com.vintan.dto.response.mypage.MyPageResponse;
import com.vintan.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageQueryService {

    private final UserRepository userRepository;
    private final QnaPostRepository qnaPostRepository;
    private final ReportRepository reportRepository;
    private final BlindCommunityPostRepository blindCommunityPostRepository;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    /** ----------------------------- Get MyPage Info ----------------------------- */
    @Transactional(readOnly = true)
    public MyPageResponse getMyPage(String userId) {
        // Fetch user
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;

        // Build Blind section (nullable)
        BlindMyPageDto blindDto = buildBlind(user);

        // Build Ask section: top 3 QnA posts
        List<AskMyPageDto> asks = qnaPostRepository.findTop3ByUser_IdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(p -> AskMyPageDto.builder()
                        .id(user.getId()) // Per spec: user ID
                        .askId(p.getPostId())
                        .title(p.getTitle())
                        .countComment(qnaPostRepository.countComments(p.getPostId()))
                        .date(p.getCreatedAt() != null ? p.getCreatedAt().format(FMT) : null)
                        .build())
                .toList();

        // Build AI Report section: latest report + total count
        long reportCount = reportRepository.countByUser_Id(user.getId());
        List<Report> latestReport = reportRepository.findByUser_IdOrderByRegDateDesc(user.getId());

        List<AiReportMyPageDto> aiReportList = latestReport.stream()
                .map(report -> AiReportMyPageDto.builder()
                        .id(report.getId())
                        .address(report.getAddress())
                        .reportCount((int) reportCount)
                        .category(report.getCategory())
                        .date(report.getRegDate() != null ? report.getRegDate().format(FMT) : null)
                        .build())
                .toList();

        // Build final response
        return MyPageResponse.builder()
                .email(user.getEmail())
                .id(user.getId())
                .blind(blindDto)
                .ask(asks)
                .name(user.getName())
                .point(user.getPoint())
                .businessNumber(user.getBusinessNumber())
                .aiReport(aiReportList)
                .build();
    }

    /** ----------------------------- Build Blind Section ----------------------------- */
    private BlindMyPageDto buildBlind(User user) {
        BlindCommunityPost post = blindCommunityPostRepository.findByUser_Id(user.getId());
        if (post == null) return null;

        // Map CategoryRate
        CategoryRateDto crDto = (post.getCategoryRate() != null)
                ? new CategoryRateDto(post.getCategoryRate())
                : null;

        // Calculate total rate if all category values exist
        Double totalRate = null;
        if (crDto != null &&
                crDto.getCleanness() != null &&
                crDto.getPeople() != null &&
                crDto.getReach() != null &&
                crDto.getRentFee() != null) {
            totalRate = (crDto.getCleanness() + crDto.getPeople() + crDto.getReach() + crDto.getRentFee()) / 4.0;
        }

        // Build Blind DTO
        return BlindMyPageDto.builder()
                .id(post.getId())
                .totalRate(totalRate)
                .title(post.getTitle())
                .address(post.getAddress())
                .date(post.getRegDate() != null ? post.getRegDate().format(FMT) : null)
                .categoryRate(crDto)
                .positive(post.getPositive())
                .negative(post.getNegative())
                .regionId(post.getRegionNo())
                .build();
    }
}
