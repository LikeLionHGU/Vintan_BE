package com.vintan.repository;

import com.vintan.domain.QnaPost;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing QnaPost entities.
 * Provides methods to fetch posts with associated comments efficiently.
 */
public interface QnaPostRepository extends JpaRepository<QnaPost, Long> {

    /**
     * Fetch all posts with their comments and user to prevent N+1 problem.
     * Results are ordered by post ID descending.
     *
     * @param regionId Region ID
     * @return List of QnaPosts
     */
    @EntityGraph(attributePaths = {"comments", "user"})
    List<QnaPost> findAllByRegionIdOrderByPostIdDesc(Long regionId);

    /**
     * Fetch a single post by ID and region along with its comments using a fetch join.
     *
     * @param postId   ID of the post
     * @param regionId Region ID
     * @return Optional of QnaPost with comments loaded
     */
    @Query("""
        select distinct p
        from QnaPost p
        left join fetch p.comments c
        where p.postId = :postId and p.regionId = :regionId
    """)
    Optional<QnaPost> findByPostIdAndRegionIdWithComments(
            @Param("postId") Long postId,
            @Param("regionId") Long regionId
    );

    /**
     * Get the top 3 most recent posts for a specific user (for MyPage view).
     *
     * @param userId User ID
     * @return List of QnaPosts
     */
    List<QnaPost> findTop3ByUser_IdOrderByCreatedAtDesc(String userId);

    /**
     * Count the number of comments for a specific post.
     *
     * @param postId ID of the post
     * @return Number of comments
     */
    @Query("select count(c) from QnaComment c where c.post.postId = :postId")
    int countComments(@Param("postId") Long postId);

    /**
     * Find a post by postId and regionId.
     *
     * @param postId   ID of the post
     * @param regionId Region ID
     * @return Optional of QnaPost
     */
    Optional<QnaPost> findByPostIdAndRegionId(Long postId, Long regionId);
}
