
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

@Service
@RequiredArgsConstructor
public class MyPageQueryService {

    private final UserRepository userRepository;
    private final QnaPostRepository qnaPostRepository;
    private final ReportRepository reportRepository;
    private final BlindCommunityPostRepository blindCommunityPostRepository;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd"); // 반드시 MM

    @Transactional(readOnly = true)
    public MyPageResponse getMyPage(String userId) {
        User u = userRepository.findById(userId).orElse(null);
        if (u == null) return null;

        // Blind 섹션 (없으면 null)
        BlindMyPageDto blindDto = buildBlind(u);

        // Ask: 내 QnA 글 상위 3개
        List<AskMyPageDto> asks = qnaPostRepository.findTop3ByUser_IdOrderByCreatedAtDesc(u.getId())
                .stream()
                .map(p -> AskMyPageDto.builder()
                        .id(u.getId()) // 명세: 사용자 id
                        .title(p.getTitle())
                        .countComment(qnaPostRepository.countComments(p.getPostId()))
                        .date(p.getCreatedAt() != null ? p.getCreatedAt().format(FMT) : null)
                        .build())
                .toList();

        // aiReport: 최근 보고서 + 전체 개수
        long reportCount = reportRepository.countByUser_Id(u.getId());
        Report latest = reportRepository.findTop1ByUser_IdOrderByRegDateDesc(u.getId());
        AiReportMyPageDto aiReport = (latest == null) ? null : AiReportMyPageDto.builder()
                .id(u.getId())
                .address(latest.getAddress())
                .reportCount((int) reportCount)
                .date(latest.getRegDate() != null ? latest.getRegDate().format(FMT) : null)
                .build();

        return MyPageResponse.builder()
                .email(u.getEmail())
                .id(u.getId())
                .Blind(blindDto)        // 필드명을 명세대로 대문자로 유지하는 경우
                .Ask(asks)              // 동일
                .name(u.getName())
                .point(u.getPoint())
                .businessNumber(u.getBusinessNumber())
                .aiReport(aiReport)
                .build();
    }

    private BlindMyPageDto buildBlind(User u) {
        BlindCommunityPost b = blindCommunityPostRepository.findByUser_Id(u.getId());
        if (b == null) return null;

        CategoryRateDto crDto = (b.getCategoryRate() != null) ? new CategoryRateDto(b.getCategoryRate()) : null;

        // totalRate: 엔티티에 필드가 없으므로 평균 산출
        Double totalRate = null;
        if (crDto != null &&
                crDto.getCleanness() != null &&
                crDto.getPeople() != null &&
                crDto.getReach() != null &&
                crDto.getRentFee() != null) {
            totalRate = (crDto.getCleanness() + crDto.getPeople() + crDto.getReach() + crDto.getRentFee()) / 4.0;
        }

        return BlindMyPageDto.builder()
                .id(b.getId())
                .totalRate(totalRate)
                .title(b.getTitle())
                .address(b.getAddress())
                .date(b.getRegDate() != null ? b.getRegDate().format(FMT) : null)
                .categoryRate(crDto) // (cleanness, people, reach, rentFee)
                .positive(b.getPositive())
                .negative(b.getNegative())
                .build();
    }
}