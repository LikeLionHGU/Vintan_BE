package com.vintan.repository;

import com.vintan.domain.BlindCommunityPost;
import com.vintan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlindCommunityPostRepository extends JpaRepository<BlindCommunityPost, Long> {
    List<BlindCommunityPost> findByRegionNo(Long regionNo);
    BlindCommunityPost findByUser_Id(String userId);
}
