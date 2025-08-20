package com.vintan.repository;

import com.vintan.domain.QnaPost;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QnaPostRepository extends JpaRepository<QnaPost, Long> {

    // 목록: 게시글 + 댓글을 함께 로딩해서 N+1 방지
    @EntityGraph(attributePaths = "comments")
    List<QnaPost> findAllByOrderByPostIdDesc();

    // 상세: 특정 게시글 + 댓글 fetch join
    @Query("""
        select distinct p
        from QnaPost p
        left join fetch p.comments c
        where p.postId = :postId
    """)
    Optional<QnaPost> findByIdWithComments(@Param("postId") Long postId);

    // 마이페이지: 내 질문글 상위 3개
    List<QnaPost> findTop3ByUser_IdOrderByCreatedAtDesc(String userId);

    // 댓글 수
    @Query("select count(c) from QnaComment c where c.post.postId = :postId")
    int countComments(@Param("postId") Long postId);
}