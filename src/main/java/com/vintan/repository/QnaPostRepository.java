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
     * Fetch all posts with their comments to prevent N+1 problem.
     * Results are ordered by post ID descending.
     */
    @EntityGraph(attributePaths = "comments")
    List<QnaPost> findAllByOrderByPostIdDesc();

    /**
     * Fetch a single post by ID along with its comments using a fetch join.
     *
     * @param postId ID of the post
     * @return Optional of QnaPost with comments loaded
     */
    @Query("""
        select distinct p
        from QnaPost p
        left join fetch p.comments c
        where p.postId = :postId
    """)
    Optional<QnaPost> findByIdWithComments(@Param("postId") Long postId);

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
}
