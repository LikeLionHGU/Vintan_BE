package com.vintan.service;


import com.vintan.domain.QnaPost;
import com.vintan.domain.QnaComment;
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

    // ✅ UserController와 동일하게 맞춤
    private static final String SESSION_USER = "loggedInUser";

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy.MM.dd");

    /** 대표화면 목록 */
    @Transactional(readOnly = true)
    public AskResponseDto getAskList() {
        // N+1 완화 위해 @EntityGraph가 QnaPostRepository.findAllByOrderByPostIdDesc()에 붙어있다고 가정
        List<QnaPost> posts = qnaPostRepository.findAllByOrderByPostIdDesc();

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

    /** 상세보기 */
    @Transactional(readOnly = true)
    public AskDetailResponseDto getAskDetail(Long postId) {
        QnaPost post = qnaPostRepository.findByIdWithComments(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        var commentsSorted = post.getComments()
                .stream()
                .sorted(Comparator.comparing(QnaComment::getId))
                .toList();

        return AskDetailResponseDto.from(post, commentsSorted);
    }

    /** 질문 글 작성 */
    @Transactional
    public SimpleSuccessResponseDto createAsk(HttpSession session, CreateAskRequestDto req) {
        try {
            // ✅ 세션에서 SessionUserDto 읽기
            SessionUserDto sessionUser = (SessionUserDto) session.getAttribute(SESSION_USER);
            if (sessionUser == null) return resp(0);

            User user = userRepository.findById(sessionUser.getId()).orElse(null);
            if (user == null) return resp(0);

            QnaPost post = QnaPost.builder()
                    .user(user)
                    .title(req.getTitle())
                    .content(req.getContent())
                    .build();

            qnaPostRepository.save(post);
            return resp(1);
        } catch (Exception e) {
            return resp(0);
        }
    }


    /** 댓글 작성 */
    @Transactional
    public SimpleSuccessResponseDto createComment(Long postId, HttpSession session, CreateCommentRequestDto req) {
        // 1) 사전 가드 (세션, 파라미터, 존재성 등)
        SessionUserDto u = (SessionUserDto) session.getAttribute("loggedInUser");
        if (u == null) return SimpleSuccessResponseDto.fail();
        var user = userRepository.findById(u.getId()).orElse(null);
        if (user == null) return SimpleSuccessResponseDto.fail();
        var post = qnaPostRepository.findById(postId).orElse(null);
        if (post == null) return SimpleSuccessResponseDto.fail();
        if (req == null || req.getComment() == null || req.getComment().isBlank()) return SimpleSuccessResponseDto.fail();

        // 2) DB 처리 (예외는 던지게 둠)
        qnaCommentRepository.save(
                QnaComment.builder()
                        .post(post)
                        .user(user)
                        .text(req.getComment())
                        .textTime(java.time.LocalDateTime.now())
                        .build()
        );
        return SimpleSuccessResponseDto.ok();
    }

    /** SimpleSuccessResponseDto(isSuccess) 값 셋팅 (DTO에 세터/생성자 없을 때) */
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