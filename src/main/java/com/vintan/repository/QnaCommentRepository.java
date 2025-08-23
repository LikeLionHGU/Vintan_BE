package com.vintan.repository;

import com.vintan.domain.QnaComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing QnaComment entities.
 * Provides CRUD operations for comments in QnA posts.
 */
@Repository
public interface QnaCommentRepository extends JpaRepository<QnaComment, Long> {
    // JpaRepository provides basic CRUD and pagination methods.
    // Additional custom queries can be added here if needed.
}
