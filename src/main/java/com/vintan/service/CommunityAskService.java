package com.vintan.service;

import com.vintan.domain.QnaPost;
import com.vintan.domain.QnaComment;
import com.vintan.domain.User;
import com.vintan.dto.request.ask.CreateAskRequestDto;
import com.vintan.dto.request.comment.CreateCommentRequestDto;
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

    /** ----------------------------- 질문 리스트 조회 ----------------------------- */
    @Transactional(readOnly = true)
    public AskResponseDto getAskList(Long regionId) {
        List<QnaPost> posts = qnaPostRepository.findAllByRegionIdOrderByPostIdDesc(regionId);

        List<AskDto> askList = posts.stream()
                .map(AskDto::from)
                .toList();

        return AskResponseDto.builder()
                .askList(askList)
                .build();
    }

    /** ----------------------------- 질문 상세 조회 ----------------------------- */
    @Transactional(readOnly = true)
    public AskDetailResponseDto getAskDetail(Long regionId, Long postId) {
        QnaPost post = qnaPostRepository.findByPostIdAndRegionIdWithComments(postId, regionId)
                .orElseThrow(() -> new IllegalArgumentException("Post does not exist."));

        List<QnaComment> sortedComments = post.getComments()
                .stream()
                .sorted(Comparator.comparing(QnaComment::getId))
                .toList();

        return AskDetailResponseDto.from(post, sortedComments);
    }

    /** ----------------------------- 질문 생성 ----------------------------- */
    @Transactional
    public SimpleSuccessResponseDto createAsk(Long regionId, HttpSession session, CreateAskRequestDto req) {
        SessionUserDto sessionUser = (SessionUserDto) session.getAttribute(SESSION_USER);
        if (sessionUser == null) return SimpleSuccessResponseDto.fail();

        User user = userRepository.findById(sessionUser.getId()).orElse(null);
        if (user == null) return SimpleSuccessResponseDto.fail();

        if (req == null || req.getTitle() == null || req.getTitle().isBlank()
                || req.getContent() == null || req.getContent().isBlank()) {
            return SimpleSuccessResponseDto.fail();
        }

        QnaPost post = QnaPost.builder()
                .user(user)
                .regionId(regionId)
                .title(req.getTitle())
                .content(req.getContent())
                .build();

        qnaPostRepository.save(post);
        return SimpleSuccessResponseDto.ok();
    }

    /** ----------------------------- 댓글 생성 ----------------------------- */
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
                        .textTime(java.time.LocalDateTime.now())
                        .build()
        );
        return SimpleSuccessResponseDto.ok();
    }
}
