package com.vintan.repository;

import com.vintan.domain.QnaPost;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QnaPostRepository extends JpaRepository<QnaPost, Long> {

    // (기존) 전체 목록 - 필요 없으면 삭제 가능
    @EntityGraph(attributePaths = {"comments", "user"})
    List<QnaPost> findAllByOrderByPostIdDesc();

    // ✅ 지역별 목록
    @EntityGraph(attributePaths = {"comments", "user"})
    List<QnaPost> findAllByRegionIdOrderByPostIdDesc(Long regionId);

    // (기존) postId만으로 상세 + 댓글 페치 — 지역 검증이 안 됨
    @Query("""
        select distinct p
        from QnaPost p
        left join fetch p.comments c
        left join fetch p.user u
        where p.postId = :postId
    """)
    Optional<QnaPost> findByIdWithComments(@Param("postId") Long postId);

    // ✅ 지역 + postId로 상세 + 댓글 페치
    @Query("""
        select distinct p
        from QnaPost p
        left join fetch p.comments c
        left join fetch p.user u
        where p.postId = :postId
          and p.regionId = :regionId
    """)
    Optional<QnaPost> findByPostIdAndRegionIdWithComments(@Param("postId") Long postId,
                                                          @Param("regionId") Long regionId);

    // ✅ 댓글 작성 시 유효성 체크용
    Optional<QnaPost> findByPostIdAndRegionId(Long postId, Long regionId);

    // 마이페이지: 내 질문글 상위 3개 (User.id = String)
    List<QnaPost> findTop3ByUser_IdOrderByCreatedAtDesc(String userId);

    @Query("select count(c) from QnaComment c where c.post.postId = :postId")
    int countComments(@Param("postId") Long postId);
}