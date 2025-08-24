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

    /** ----------------------------- List of Questions ----------------------------- */
    @Transactional(readOnly = true)
    public AskResponseDto getAskList(Long regionId) {
        // Use @EntityGraph in repository to prevent N+1 queries
        List<QnaPost> posts = qnaPostRepository.findAllByRegionIdOrderByPostIdDesc(regionId);

        // Map posts to DTOs
        List<AskDto> askList = posts.stream()
                .map(AskDto::from)
                .toList();

        return AskResponseDto.builder()
                .askList(askList)
                .build();
    }

    /** ----------------------------- Question Detail ----------------------------- */
    @Transactional(readOnly = true)
    public AskDetailResponseDto getAskDetail(Long regionId, Long postId) {
        // Fetch post with comments using fetch join
        QnaPost post = qnaPostRepository.findByPostIdAndRegionIdWithComments(postId, regionId)
                .orElseThrow(() -> new IllegalArgumentException("Post does not exist."));

        // Sort comments by ID
        List<QnaComment> sortedComments = post.getComments()
                .stream()
                .sorted(Comparator.comparing(QnaComment::getId))
                .toList();

        return AskDetailResponseDto.from(post, sortedComments);
    }

    /** ----------------------------- Create Question ----------------------------- */
    @Transactional
    public SimpleSuccessResponseDto createAsk(Long regionId, HttpSession session, CreateAskRequestDto req) {
        try {
            // Get logged-in user from session
            SessionUserDto sessionUser = (SessionUserDto) session.getAttribute(SESSION_USER);
            if (sessionUser == null) return resp(0);

            // Fetch user entity
            User user = userRepository.findById(sessionUser.getId()).orElse(null);
            if (user == null) return resp(0);

            if (req == null || req.getTitle() == null || req.getTitle().isBlank()
                    || req.getContent() == null || req.getContent().isBlank()) {
                return resp(0);
            }

            // Save new post
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

    /** ----------------------------- Create Comment ----------------------------- */
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
                        .build()
        );
        return SimpleSuccessResponseDto.ok();
    }

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
