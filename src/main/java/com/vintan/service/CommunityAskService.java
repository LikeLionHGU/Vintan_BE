package com.vintan.service;

import com.vintan.domain.QnaComment;
import com.vintan.domain.QnaPost;
import com.vintan.domain.User;
import com.vintan.dto.request.ask.CreateAskRequestDto;
import com.vintan.dto.request.ask.CreateCommentRequestDto;
import com.vintan.dto.response.ask.AskDetailResponseDto;
import com.vintan.dto.response.ask.AskDto;
import com.vintan.dto.response.ask.AskResponseDto;
import com.vintan.dto.response.ask.SimpleSuccessResponseDto;
import com.vintan.dto.response.user.SessionUserDto;
import com.vintan.repository.QnaCommentRepository;
import com.vintan.repository.QnaPostRepository;
import com.vintan.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityAskService {

    private final QnaPostRepository qnaPostRepository;
    private final QnaCommentRepository qnaCommentRepository;
    private final UserRepository userRepository;

    private static final String SESSION_USER = "loggedInUser";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    /** 질문 목록 (지역별) */
    @Transactional(readOnly = true)
    public AskResponseDto getAskList(Long regionId) {
        List<QnaPost> posts = qnaPostRepository.findAllByRegionIdOrderByPostIdDesc(regionId);

        List<AskDto> askList = posts.stream()
                .map(p -> AskDto.builder()
                        .id(p.getPostId())
                        .title(p.getTitle())
                        .userId(p.getUser().getId())
                        .numberOfComments(p.getComments() == null ? 0 : p.getComments().size())
                        .date(p.getCreatedAt() == null ? null : p.getCreatedAt().format(DATE_FMT))
                        .build())
                .toList();

        return AskResponseDto.builder().askList(askList).build();
    }

    /** 질문 상세 (지역+게시글 보장) */
    @Transactional(readOnly = true)
    public AskDetailResponseDto getAskDetail(Long regionId, Long postId) {
        QnaPost post = qnaPostRepository.findByPostIdAndRegionIdWithComments(postId, regionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지역에 게시글이 존재하지 않습니다."));

        var commentsSorted = post.getComments()
                .stream()
                .sorted(Comparator.comparing(QnaComment::getId))
                .toList();

        return AskDetailResponseDto.from(post, commentsSorted);
    }

    /** 질문 글 작성 (지역 설정) */
    @Transactional
    public SimpleSuccessResponseDto createAsk(Long regionId, HttpSession session, CreateAskRequestDto req) {
        try {
            SessionUserDto sessionUser = (SessionUserDto) session.getAttribute(SESSION_USER);
            if (sessionUser == null) return resp(0);

            User user = userRepository.findById(sessionUser.getId()).orElse(null);
            if (user == null) return resp(0);

            if (req == null || req.getTitle() == null || req.getTitle().isBlank()
                    || req.getContent() == null || req.getContent().isBlank()) {
                return resp(0);
            }

            QnaPost post = QnaPost.builder()
                    .user(user)
                    .regionId(regionId)
                    .title(req.getTitle())
                    .content(req.getContent())
                    .build();

            qnaPostRepository.save(post);
            return resp(1);
        } catch (Exception e) {
            return resp(0);
        }
    }

    /** 댓글 작성 (지역/게시글 유효성 확인) */
    @Transactional
    public SimpleSuccessResponseDto createComment(Long regionId, Long postId, HttpSession session, CreateCommentRequestDto req) {
        SessionUserDto u = (SessionUserDto) session.getAttribute(SESSION_USER);
        if (u == null) return SimpleSuccessResponseDto.fail();

        User user = userRepository.findById(u.getId()).orElse(null);
        if (user == null) return SimpleSuccessResponseDto.fail();

        QnaPost post = qnaPostRepository.findByPostIdAndRegionId(postId, regionId).orElse(null);
        if (post == null) return SimpleSuccessResponseDto.fail();

        if (req == null || req.getComment() == null || req.getComment().isBlank())
            return SimpleSuccessResponseDto.fail();

        qnaCommentRepository.save(
                QnaComment.builder()
                        .post(post)
                        .user(user)
                        .text(req.getComment())
                        // textTime은 @CreatedDate로 자동 셋업
                        .build()
        );
        return SimpleSuccessResponseDto.ok();
    }

    /** SimpleSuccessResponseDto(isSuccess) 편의 설정 (세터/생성자 없을 때) */
    private SimpleSuccessResponseDto resp(int v) {
        SimpleSuccessResponseDto dto = new SimpleSuccessResponseDto();
        try {
            Field f = SimpleSuccessResponseDto.class.getDeclaredField("isSuccess");
            f.setAccessible(true);
            f.setInt(dto, v);
        } catch (Exception ignored) { }
        return dto;
    }
}
