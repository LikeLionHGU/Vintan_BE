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

    /** ----------------------------- List of Questions ----------------------------- */
    @Transactional(readOnly = true)
    public AskResponseDto getAskList() {
        // Use @EntityGraph in repository to prevent N+1 queries
        List<QnaPost> posts = qnaPostRepository.findAllByOrderByPostIdDesc();

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
    public AskDetailResponseDto getAskDetail(Long postId) {
        // Fetch post with comments using fetch join
        QnaPost post = qnaPostRepository.findByIdWithComments(postId)
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
    public SimpleSuccessResponseDto createAsk(HttpSession session, CreateAskRequestDto req) {
        // Get logged-in user from session
        SessionUserDto sessionUser = (SessionUserDto) session.getAttribute(SESSION_USER);
        if (sessionUser == null) return SimpleSuccessResponseDto.fail();

        // Fetch user entity
        User user = userRepository.findById(sessionUser.getId()).orElse(null);
        if (user == null) return SimpleSuccessResponseDto.fail();

        // Save new post
        QnaPost post = QnaPost.builder()
                .user(user)
                .title(req.getTitle())
                .content(req.getContent())
                .build();

        qnaPostRepository.save(post);
        return SimpleSuccessResponseDto.ok();
    }

    /** ----------------------------- Create Comment ----------------------------- */
    @Transactional
    public SimpleSuccessResponseDto createComment(Long postId, HttpSession session, CreateCommentRequestDto req) {
        // Validate session and request
        SessionUserDto sessionUser = (SessionUserDto) session.getAttribute(SESSION_USER);
        if (sessionUser == null || req == null || req.getComment().isBlank()) {
            return SimpleSuccessResponseDto.fail();
        }

        // Fetch user and post entities
        User user = userRepository.findById(sessionUser.getId()).orElse(null);
        QnaPost post = qnaPostRepository.findById(postId).orElse(null);
        if (user == null || post == null) return SimpleSuccessResponseDto.fail();

        // Save comment
        QnaComment comment = QnaComment.builder()
                .post(post)
                .user(user)
                .text(req.getComment())
                .textTime(java.time.LocalDateTime.now())
                .build();

        qnaCommentRepository.save(comment);
        return SimpleSuccessResponseDto.ok();
    }
}
