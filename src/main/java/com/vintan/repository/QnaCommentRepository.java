package com.vintan.repository;

import com.vintan.domain.QnaComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaCommentRepository extends JpaRepository<QnaComment, Long> {
    long countByUser_Id(String userId);
}