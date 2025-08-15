package com.vintan.repository;

import com.vintan.domain.BlindCommunityPost;
import com.vintan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlindCommunityPostRepository extends JpaRepository<BlindCommunityPost, Long> {
}
